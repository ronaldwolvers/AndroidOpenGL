package com.androidopengl.app.opengl

import android.content.Context
import android.opengl.GLES20
import com.androidopengl.app.logError
import com.androidopengl.app.logEvent
import com.androidopengl.app.logWarning
import com.androidopengl.app.utils.convertInputStreamToString
import java.io.IOException
import java.io.InputStream

fun loadShader(type: Int, shaderCode: String): Int {

    logEvent(event = "loadShader() is being called...\n" +
                "type: ${"%x".format(type).uppercase()}"
    )

    return GLES20.glCreateShader(type).also { shader ->
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
    }
}

private fun compileShaderFromShaderCode(type: Int, shaderCode: String): Int {

    if (type != GLES20.GL_FRAGMENT_SHADER && type != GLES20.GL_VERTEX_SHADER) {
        logWarning(warning = "Attempting to compile a shader that is not a fragment or a vertex shader...")
    }

    return GLES20.glCreateShader(type).also { createShaderResult ->

        if (createShaderResult == 0 || createShaderResult == GLES20.GL_INVALID_ENUM) {
            logError(error = "Something went wrong while creating a shader...")
            return createShaderResult
        }

        GLES20.glShaderSource(createShaderResult, shaderCode)
        GLES20.glCompileShader(createShaderResult)
    }
}

@Throws(IOException::class)
fun loadShaderFromFile(context: Context? = null, type: Int, pathInAssetsFolder: String): Int {

    logEvent(
        event = "loadShaderFromFile() is being called...\t\t" + "type: ${
            "%x".format(type).uppercase()
        }"
    )

    if (context == null) {

        val event = "Tried to load a shader from the assets folder, but there is no context!"
        logError(
            error = event
        )
        throw java.lang.NullPointerException(event)
    }

    var inputStream: InputStream? = null
    val shaderCode: String?
    try {
        inputStream = context.assets.open(pathInAssetsFolder)
        shaderCode = convertInputStreamToString(context.assets.open(pathInAssetsFolder))
    } finally {
        inputStream?.close()
    }

    return compileShaderFromShaderCode(type, shaderCode = shaderCode!!)
}