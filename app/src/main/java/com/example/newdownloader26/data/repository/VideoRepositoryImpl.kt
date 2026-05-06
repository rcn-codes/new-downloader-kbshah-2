package com.example.newdownloader26.data.repository

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.newdownloader26.R
import com.example.newdownloader26.data.remote.VideoApiService
import com.example.newdownloader26.data.remote.dto.DownloadRequestDto
import com.example.newdownloader26.domain.model.DownloadPhase
import com.example.newdownloader26.domain.model.DownloadProgress
import com.example.newdownloader26.domain.model.DownloadRequest
import com.example.newdownloader26.domain.model.DownloadResult
import com.example.newdownloader26.domain.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class VideoRepositoryImpl(
    private val context: Context,
    private val apiService: VideoApiService
) : VideoRepository {
    override suspend fun downloadVideo(
        request: DownloadRequest,
        onProgress: (DownloadProgress) -> Unit
    ): DownloadResult = withContext(Dispatchers.IO) {
        try {
            onProgress(
                DownloadProgress(
                    phase = DownloadPhase.PREPARING,
                    message = "Preparing video on server..."
                )
            )

            val response = apiService.downloadVideo(
                request = DownloadRequestDto(url = request.url),
                range = null
            )

            if (!response.isSuccessful || response.body() == null) {
                val errorMessage = "Server failed to process this video."
                onProgress(DownloadProgress(phase = DownloadPhase.FAILED, message = errorMessage))
                return@withContext DownloadResult(false, errorMessage, null)
            }

            saveResponseBody(request, response, onProgress)
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Download failed."
            onProgress(DownloadProgress(phase = DownloadPhase.FAILED, message = errorMessage))
            DownloadResult(false, errorMessage, null)
        }
    }

    private fun saveResponseBody(
        request: DownloadRequest,
        response: Response<ResponseBody>,
        onProgress: (DownloadProgress) -> Unit
    ): DownloadResult {
        val body = response.body() ?: return DownloadResult(false, "Empty server response.", null)
        val fileName = resolveFileName(response, body, request.url)
        val target = createOutputTarget(fileName)
        val totalBytes = body.contentLength()
        val buffer = ByteArray(8 * 1024)
        var downloadedBytes = 0L

        try {
            body.byteStream().use { input ->
                target.outputStream.use { output ->
                    var readCount: Int

                    onProgress(
                        DownloadProgress(
                            phase = DownloadPhase.DOWNLOADING,
                            downloadedBytes = 0L,
                            totalBytes = totalBytes,
                            message = "Downloading..."
                        )
                    )

                    while (input.read(buffer).also { readCount = it } != -1) {
                        output.write(buffer, 0, readCount)
                        downloadedBytes += readCount
                        onProgress(
                            DownloadProgress(
                                phase = DownloadPhase.DOWNLOADING,
                                downloadedBytes = downloadedBytes,
                                totalBytes = totalBytes,
                                message = "Downloading..."
                            )
                        )
                    }

                    output.flush()
                }
            }

            onProgress(
                DownloadProgress(
                    phase = DownloadPhase.SAVING,
                    totalBytes = totalBytes,
                    message = "Saving to Downloads..."
                )
            )

            onProgress(
                DownloadProgress(
                    phase = DownloadPhase.COMPLETED,
                    totalBytes = totalBytes,
                    message = "Download complete."
                )
            )

            return DownloadResult(
                success = true,
                message = "Saved to ${target.displayPath}",
                savedPath = target.displayPath
            )
        } catch (e: Exception) {
            cleanupTarget(target)
            throw e
        }
    }

    private fun resolveFileName(
        response: Response<ResponseBody>,
        body: ResponseBody,
        sourceUrl: String
    ): String {
        val platformPrefix = detectPlatformPrefix(sourceUrl)
        val header = response.headers()["Content-Disposition"]
        val fromHeader = header
            ?.split(";")
            ?.firstOrNull { it.trim().startsWith("filename=") }
            ?.substringAfter("=")
            ?.trim('"', ' ')
            ?.takeIf { it.isNotBlank() }
        if (!fromHeader.isNullOrBlank()) {
            return applyPrefixIfNeeded(fromHeader, platformPrefix)
        }

        val extension = body.contentType()?.subtype?.takeIf { it.isNotBlank() } ?: "mp4"
        val generated = "video_${System.currentTimeMillis()}.$extension"
        return applyPrefixIfNeeded(generated, platformPrefix)
    }

    private fun applyPrefixIfNeeded(fileName: String, prefix: String?): String {
        if (prefix.isNullOrBlank()) return fileName
        val lower = fileName.lowercase()
        if (lower.startsWith(prefix)) return fileName
        return "${prefix}${fileName}"
    }

    private fun detectPlatformPrefix(url: String): String? {
        val host = runCatching { Uri.parse(url).host.orEmpty().lowercase() }.getOrDefault("")
        return when {
            host.contains("tiktok") -> "tiktok_"
            host.contains("instagram") || host.contains("instagr.am") -> "instagram_"
            host.contains("facebook") || host.contains("fb.watch") -> "facebook_"
            host.contains("linkedin") -> "linkedin_"
            host == "x.com" || host.endsWith(".x.com") || host.contains("twitter") -> "x_"
            else -> null
        }
    }

    private fun createOutputTarget(fileName: String): DownloadTarget {
        val appFolder = context.getString(R.string.app_name)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "${Environment.DIRECTORY_DOWNLOADS}/$appFolder"
                )
            }
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                ?: throw IllegalStateException("Cannot create file in Downloads.")
            val stream = resolver.openOutputStream(uri)
                ?: throw IllegalStateException("Cannot open output stream.")
            DownloadTarget(
                outputStream = stream,
                displayPath = "Download/$appFolder/$fileName",
                mediaUri = uri.toString()
            )
        } else {
            val downloadsRoot =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val appDirectory = File(downloadsRoot, appFolder)
            if (!appDirectory.exists()) {
                appDirectory.mkdirs()
            }
            val file = File(appDirectory, fileName)
            DownloadTarget(
                outputStream = FileOutputStream(file),
                displayPath = file.absolutePath
            )
        }
    }

    private fun cleanupTarget(target: DownloadTarget) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return
        val uriText = target.mediaUri ?: return
        runCatching {
            context.contentResolver.delete(android.net.Uri.parse(uriText), null, null)
        }
    }

    private data class DownloadTarget(
        val outputStream: OutputStream,
        val displayPath: String,
        val mediaUri: String? = null
    )
}
