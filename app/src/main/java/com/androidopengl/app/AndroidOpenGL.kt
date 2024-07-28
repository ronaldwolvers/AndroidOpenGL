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

const val EGL_CONTEXT_CLIENT_VERSION = 0x3098
const val PREFERRED_OPENGL_VERSION = 3.2
const val FALLBACK_OPENGL_VERSION = 2.0

class AndroidOpenGL: Application()