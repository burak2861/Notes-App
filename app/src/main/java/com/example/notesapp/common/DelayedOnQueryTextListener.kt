package com.example.notesapp.common

import android.os.Handler
import android.os.Looper
import android.widget.SearchView

abstract class DelayedOnQueryTextListener : SearchView.OnQueryTextListener {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    override fun onQueryTextSubmit(s: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(s: String?): Boolean {
        runnable?.let { handler.removeCallbacks(it) }
        runnable = Runnable { onDelayerQueryTextChange(s) }
        runnable?.let { handler.postDelayed(it, DELAY_MILLIS) }
        return true
    }

    abstract fun onDelayerQueryTextChange(query: String?)

    companion object {
        private const val DELAY_MILLIS = 500L
    }
}
