package com.androidopengl.app.opengl.triangle

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.glVertexAttribPointer
import com.androidopengl.app.opengl.loadShaderFromFile
import com.androidopengl.app.logError
import com.androidopengl.app.logEvent
import com.androidopengl.app.opengl.COORDINATES_PER_VERTEX
import com.androidopengl.app.opengl.OpenGLObject

class TriangleOpenGLObject(
    private val context: Context? = null,
    private val color: FloatArray,
    val coordinates: FloatArray
) : OpenGLObject(coordinates, context) {

    private var programHandle: Int = -1

    init {
        createProgram()
    }

    override fun createProgram() {
        val fragmentShaderHandle = loadShaderFromFile(
            context,
            GLES20.GL_FRAGMENT_SHADER, "shaders/triangle/triangle.fshader"
        )

        val vertexShaderHandle = loadShaderFromFile(
            context,
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

    private var mVPMatrixUniformHandle: Int = -1
    private var colorUniformHandle: Int = -1

    private var positionAttributeHandle: Int = -1

    fun draw(mvpMatrix: FloatArray? = null) {

        logEvent(
            event = "TriangleOpenGLObject.draw() is being called..."
        )

        if (mvpMatrix != null) {
            mVPMatrixUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix")
            logEvent(
                event = "mVPMatrixUniformHandle: $mVPMatrixUniformHandle\n"
            )
            GLES20.glUniformMatrix4fv(mVPMatrixUniformHandle, 1, false, mvpMatrix, 0)
        }

        colorUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Color")
        logEvent(
            event = "colorUniformHandle: $colorUniformHandle\n"
        )

        if (colorUniformHandle != -1 && colorUniformHandle != GLES20.GL_INVALID_VALUE && colorUniformHandle != GLES20.GL_INVALID_OPERATION) {
            GLES20.glUniform4fv(colorUniformHandle, 1, color, 0)
        } else {
            logError(
                error = "Something went wrong while trying to inject a value for uniform u_Color...\n"
            )
            return
        }

        positionAttributeHandle = GLES20.glGetAttribLocation(programHandle, "a_Position")
        logEvent(
            event = "positionAttributeHandle: $positionAttributeHandle\n"
        )

        if (positionAttributeHandle != -1 && positionAttributeHandle != GLES20.GL_INVALID_VALUE && positionAttributeHandle != GLES20.GL_INVALID_OPERATION) {

            GLES20.glEnableVertexAttribArray(positionAttributeHandle)

            glVertexAttribPointer(
                positionAttributeHandle,
                COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
            if (vertexCount == 4) {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 1, vertexCount)
            }

            GLES20.glDisableVertexAttribArray(positionAttributeHandle)

        } else {
            logError(
                error = "Something went wrong while trying to inject a value for attribute a_Position...\n"
            )
            return
        }
    }

}