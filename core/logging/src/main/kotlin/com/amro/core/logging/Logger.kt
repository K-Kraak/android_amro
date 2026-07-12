package com.amro.core.logging

import android.util.Log

interface Logger {
    fun error(message: String, throwable: Throwable? = null);
    fun info(message: String)
}

class AndroidLogger(private val tag: String = "AMRO") : Logger {
    override fun error(message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }

    override fun info(message: String) {
        Log.i(tag, message)
    }
}