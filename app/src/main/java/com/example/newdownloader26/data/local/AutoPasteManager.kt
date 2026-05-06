package com.example.newdownloader26.data.local

import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import com.example.newdownloader26.presentation.downloader.DownloadPlatform

/**
 * Reads the system clipboard and applies the user's auto-paste preference from [SettingsPreferences].
 * When auto-paste is off, callers should not invoke auto behavior; manual paste remains available in the UI.
 */
class AutoPasteManager(
    private val context: Context,
    private val settingsPreferences: SettingsPreferences
) {

    val isAutoPasteEnabled: Boolean
        get() = settingsPreferences.getAutoDetectLinkEnabled()

    fun readPrimaryClipPlainText(): String? {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = cm.primaryClip ?: return null
        if (clip.itemCount <= 0) return null
        val text = clip.getItemAt(0).coerceToText(context)?.toString()?.trim().orEmpty()
        return text.takeIf { it.isNotEmpty() }
    }

    companion object {
        private val URL_REGEX = Regex("""https?://\S+""", RegexOption.IGNORE_CASE)

        fun extractFirstHttpUrl(text: String): String? {
            val raw = URL_REGEX.find(text)?.value ?: return null
            return raw.trimEnd('.', ',', ';', '!', ')', ']', '}', '"', '\'', '»')
        }

        fun isUrlForPlatform(url: String, platform: DownloadPlatform): Boolean {
            val host = runCatching { Uri.parse(url).host?.lowercase() }.getOrNull() ?: return false
            fun endsWithAny(vararg suffixes: String): Boolean = suffixes.any { host == it || host.endsWith(".$it") }

            return when (platform) {
                DownloadPlatform.TIKTOK ->
                    endsWithAny("tiktok.com", "vm.tiktok.com", "m.tiktok.com")

                DownloadPlatform.INSTAGRAM ->
                    endsWithAny("instagram.com", "www.instagram.com", "instagr.am")

                DownloadPlatform.FACEBOOK ->
                    endsWithAny("facebook.com", "m.facebook.com", "fb.watch")

                DownloadPlatform.LINKEDIN ->
                    endsWithAny("linkedin.com", "www.linkedin.com")

                DownloadPlatform.X ->
                    endsWithAny("x.com", "www.x.com", "twitter.com", "www.twitter.com", "t.co")
            }
        }
    }
}
