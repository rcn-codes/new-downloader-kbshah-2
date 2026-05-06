package com.example.newdownloader26.presentation.downloader

import android.content.ClipboardManager
import android.content.Context
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

/**
 * Fires [onAutoPaste] when this screen is (re)composed, when the Activity resumes, after the
 * window regains focus (returning from another app while the lifecycle can stay RESUMED), when the
 * primary clip changes, and one frame after resume/focus so the system clipboard has the latest clip.
 * [onAutoPaste] should no-op when auto-paste is off.
 */
@Composable
fun DownloaderAutoPasteLifecycleEffect(onAutoPaste: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val view = LocalView.current
    val scope = rememberCoroutineScope()
    val latestPaste by rememberUpdatedState(onAutoPaste)

    suspend fun pasteNowAndAfterFrame() {
        latestPaste()
        awaitFrame()
        latestPaste()
    }

    LaunchedEffect(Unit) {
        pasteNowAndAfterFrame()
    }
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            pasteNowAndAfterFrame()
        }
    }
    DisposableEffect(context) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val listener = ClipboardManager.OnPrimaryClipChangedListener { latestPaste() }
        cm.addPrimaryClipChangedListener(listener)
        onDispose { cm.removePrimaryClipChangedListener(listener) }
    }
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnWindowFocusChangeListener { hasFocus ->
            if (hasFocus) {
                scope.launch {
                    latestPaste()
                    awaitFrame()
                    latestPaste()
                }
            }
        }
        val observer = view.viewTreeObserver
        if (observer.isAlive) {
            observer.addOnWindowFocusChangeListener(listener)
        }
        onDispose {
            if (observer.isAlive) {
                observer.removeOnWindowFocusChangeListener(listener)
            }
        }
    }
}
