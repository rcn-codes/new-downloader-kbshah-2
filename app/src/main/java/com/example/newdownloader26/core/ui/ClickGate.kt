package com.example.newdownloader26.core.ui

import android.os.SystemClock
import java.util.concurrent.atomic.AtomicLong

/**
 * Global click gate for the whole app.
 *
 * Any UI action that should not be triggered twice quickly should go through this gate.
 */
object ClickGate {
    private val lastClickAtMs = AtomicLong(0L)

    fun tryAcquire(debounceMs: Long): Boolean {
        val now = SystemClock.elapsedRealtime()
        while (true) {
            val prev = lastClickAtMs.get()
            if (now - prev < debounceMs) return false
            if (lastClickAtMs.compareAndSet(prev, now)) return true
        }
    }
}

