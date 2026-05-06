package com.example.newdownloader26.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class SourcePlatform {
    ALL,
    TIKTOK,
    INSTAGRAM,
    FACEBOOK,
    LINKEDIN,
    X,
    UNKNOWN
}

@Serializable
data class DownloadedVideo(
    val id: String,
    val sourceUrl: String,
    val sourcePlatform: SourcePlatform,
    val fileName: String,
    val savedPath: String,
    val createdAtMillis: Long,
    val sizeBytes: Long? = null,
    val playUri: String? = null
)
