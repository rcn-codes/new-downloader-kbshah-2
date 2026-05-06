package com.example.newdownloader26.data.remote

import com.example.newdownloader26.data.remote.dto.DownloadRequestDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Body
import retrofit2.http.POST

interface VideoApiService {
    @POST("process")
    suspend fun downloadVideo(
        @Body request: DownloadRequestDto,
        @Header("Range") range: String? = null
    ): Response<ResponseBody>
}
