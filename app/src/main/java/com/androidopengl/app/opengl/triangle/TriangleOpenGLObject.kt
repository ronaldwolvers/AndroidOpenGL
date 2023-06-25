package com.androidopengl.app.opengl.triangle

import android.content.Context
import android.opengl.GLES20
import com.androidopengl.app.loadShaderFromFile
import com.androidopengl.app.logError
import com.androidopengl.app.logEvent
import com.androidopengl.app.opengl.OpenGLObject

class TriangleOpenGLObject(private val context: Context, color: FloatArray, coordinates: FloatArray): OpenGLObject (coordinates, context) {

    private var programHandle: Int = -1

    init {
        createProgram()
    }

    override fun createProgram() {
        val fragmentShaderHandle = loadShaderFromFile(context,
            GLES20.GL_FRAGMENT_SHADER, "shaders/triangle/triangle.fshader"
        )

        val vertexShaderHandle = loadShaderFromFile(context,
            GLES20.GL_VERTEX_SHADER, "shaders/triangle/triangle.vshader"
        )

        programHandle = GLES20.glCreateProgram()

        logEvent(
            event = "programHandle: $programHandle\n"
        )

        if (programHandle == 0) {
            logError(
                error = "Something went wrong while calling glCreateProgram()..."
            )
            return
        }

        GLES20.glAttachShader(programHandle, vertexShaderHandle)
        GLES20.glAttachShader(programHandle, fragmentShaderHandle)
        GLES20.glLinkProgram(programHandle)
        GLES20.glUseProgram(programHandle)
    }
}