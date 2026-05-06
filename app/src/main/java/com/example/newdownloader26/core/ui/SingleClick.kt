package com.example.newdownloader26.core.ui

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

private const val DEFAULT_DEBOUNCE_MS = 1000L

/**
 * Debounced click handler for Compose.
 *
 * - Ignores subsequent clicks within [debounceMs].
 * - Uses monotonic time ([SystemClock.elapsedRealtime]) to avoid wall-clock changes.
 */
@Stable
fun Modifier.singleClick(
    enabled: Boolean = true,
    debounceMs: Long = DEFAULT_DEBOUNCE_MS,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
    onClick: () -> Unit,
): Modifier = composed {
    val src = interactionSource ?: remember { MutableInteractionSource() }
    clickable(
        enabled = enabled,
        interactionSource = src,
        indication = indication,
    ) {
        if (ClickGate.tryAcquire(debounceMs)) onClick()
    }
}

/**
 * Debounced wrapper for non-Modifier click callbacks (e.g., `onClick = singleClick { ... }`).
 *
 * Prefer [Modifier.singleClick] when you already have a Modifier.
 */
@Stable
fun singleClick(
    debounceMs: Long = DEFAULT_DEBOUNCE_MS,
    enabled: () -> Boolean = { true },
    onClick: () -> Unit,
): () -> Unit {
    return {
        if (enabled() && ClickGate.tryAcquire(debounceMs)) {
            onClick()
        }
    }
}

