package com.example.newdownloader26.data.local

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.newdownloader26.R
import com.example.newdownloader26.domain.model.DownloadedVideo
import com.example.newdownloader26.domain.model.SourcePlatform

class DeviceVideoStore(
    private val context: Context
) {

    fun listVideos(): List<DownloadedVideo> {
        val resolver = context.contentResolver
        val projection = buildList {
            add(MediaStore.MediaColumns._ID)
            add(MediaStore.MediaColumns.DISPLAY_NAME)
            add(MediaStore.MediaColumns.SIZE)
            add(MediaStore.MediaColumns.DATE_ADDED)
            add(MediaStore.MediaColumns.MIME_TYPE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                add(MediaStore.MediaColumns.RELATIVE_PATH)
            } else {
                @Suppress("DEPRECATION")
                add(MediaStore.MediaColumns.DATA)
            }
        }.toTypedArray()

        val appFolder = context.getString(R.string.app_name)
        val sortOrder = "${MediaStore.MediaColumns.DATE_ADDED} DESC"

        val query = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val relativePath = "${Environment.DIRECTORY_DOWNLOADS}/$appFolder/"
            resolver.query(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                projection,
                "${MediaStore.MediaColumns.RELATIVE_PATH}=? AND ${MediaStore.MediaColumns.MIME_TYPE} LIKE ?",
                arrayOf(relativePath, "video/%"),
                sortOrder
            )
        } else {
            @Suppress("DEPRECATION")
            val downloadPath = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath}/$appFolder/"
            @Suppress("DEPRECATION")
            resolver.query(
                MediaStore.Files.getContentUri("external"),
                projection,
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? AND ${MediaStore.MediaColumns.DATA} LIKE ?",
                arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(), "$downloadPath%"),
                sortOrder
            )
        }

        query?.use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val nameIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
            val dateIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)
            val pathIndex = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH)
            } else {
                @Suppress("DEPRECATION")
                cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            }

            val result = mutableListOf<DownloadedVideo>()
            while (cursor.moveToNext()) {
                val mediaId = cursor.getLong(idIndex)
                val fileName = cursor.getString(nameIndex) ?: "video_$mediaId.mp4"
                val size = cursor.getLong(sizeIndex)
                val dateAddedMillis = cursor.getLong(dateIndex) * 1000L
                val path = cursor.getString(pathIndex).orEmpty()
                val contentUri = ContentUris.withAppendedId(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        MediaStore.Downloads.EXTERNAL_CONTENT_URI
                    } else {
                        MediaStore.Files.getContentUri("external")
                    },
                    mediaId
                )
                result += DownloadedVideo(
                    id = mediaId.toString(),
                    sourceUrl = "",
                    sourcePlatform = detectPlatform(fileName),
                    fileName = fileName,
                    savedPath = path,
                    createdAtMillis = dateAddedMillis,
                    sizeBytes = size,
                    playUri = contentUri.toString()
                )
            }
            return result
        }

        return emptyList()
    }

    fun deleteByUri(uriString: String): Boolean {
        return runCatching {
            context.contentResolver.delete(android.net.Uri.parse(uriString), null, null) > 0
        }.getOrDefault(false)
    }

    private fun detectPlatform(text: String): SourcePlatform {
        val lower = text.lowercase().trim()
        return when {
            lower.startsWith("tiktok_") -> SourcePlatform.TIKTOK
            lower.startsWith("instagram_") -> SourcePlatform.INSTAGRAM
            lower.startsWith("facebook_") -> SourcePlatform.FACEBOOK
            lower.startsWith("linkedin_") -> SourcePlatform.LINKEDIN
            lower.startsWith("x_") -> SourcePlatform.X
            "tiktok" in lower -> SourcePlatform.TIKTOK
            "instagram" in lower || "instagr" in lower -> SourcePlatform.INSTAGRAM
            "facebook" in lower || "fb.watch" in lower -> SourcePlatform.FACEBOOK
            "linkedin" in lower -> SourcePlatform.LINKEDIN
            "twitter" in lower -> SourcePlatform.X
            else -> SourcePlatform.UNKNOWN
        }
    }
}
