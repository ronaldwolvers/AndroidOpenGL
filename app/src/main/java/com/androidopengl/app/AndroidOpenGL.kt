package com.androidopengl.app

import android.app.Application
import android.util.Log

const val ANDROID_OPENGL_LOGGING_TAG = "AndroidOpenGL"

fun logWarning(warning: String) {
    Log.w(ANDROID_OPENGL_LOGGING_TAG, warning)
}

fun logError(error: String) {
    Log.e(ANDROID_OPENGL_LOGGING_TAG, error)
}

fun logEvent(event: String) {
    Log.i(ANDROID_OPENGL_LOGGING_TAG, event)
}

class AndroidOpenGL: Application()