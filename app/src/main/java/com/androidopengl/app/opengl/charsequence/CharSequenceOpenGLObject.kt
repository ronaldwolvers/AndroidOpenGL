package com.androidopengl.app.opengl.charsequence

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.glVertexAttribPointer
import androidx.core.content.ContextCompat
import com.androidopengl.app.R
import com.androidopengl.app.opengl.loadShaderFromFile
import com.androidopengl.app.logError
import com.androidopengl.app.logEvent
import com.androidopengl.app.opengl.COORDINATES_PER_VERTEX
import com.androidopengl.app.opengl.OpenGLObject
import com.androidopengl.app.opengl.TEXTURE_COORDINATES_PER_VERTEX
import com.androidopengl.app.utils.colorToFloatArray
import com.androidopengl.app.utils.createBitmapFromCharSequence

private const val DEFAULT_TEXTSIZE: Float = 60F

class CharSequenceOpenGLObject(
    private val context: Context,
    coordinates: FloatArray,
    private val charSequence: CharSequence,
    private val heightInPx: Int,
    private val widthInPx: Int,
    private val backgroundColor: FloatArray? = ContextCompat.getColor(context, R.color.white)
        .colorToFloatArray(),
    textSize: Float = DEFAULT_TEXTSIZE
) : OpenGLObject(coordinates, context) {

    private val textureDataHandle = context.let {
        val bitmap = createBitmapFromCharSequence(
            charSequence,
            heightInPx,
            widthInPx,
            backgroundColor,
            textSize
        )
        loadTextureFromBitmap(bitmap)
    }

    private var programHandle: Int = -1

    init {
        createProgram()
    }

    override fun createProgram() {
        val fragmentShaderHandle = loadShaderFromFile(
            context,
            GLES20.GL_FRAGMENT_SHADER, "shaders/charsequence/charsequence.fshader"
        )

        val vertexShaderHandle = loadShaderFromFile(
            context,
            GLES20.GL_VERTEX_SHADER, "shaders/charsequence/charsequence.vshader"
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
    private var textureUniformHandle: Int = -1

    private var positionAttributeHandle: Int = -1
    private var textureCoordinateAttributeHandle: Int = -1

    fun draw(mvpMatrix: FloatArray? = null) {

        logEvent(
            event = "CharSequenceOpenGLObject.draw() is being called..."
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
        logEvent(
            event = "color: $backgroundColor"
        )

        if (backgroundColor != null) {

            if (colorUniformHandle != -1 && colorUniformHandle != GLES20.GL_INVALID_VALUE && colorUniformHandle != GLES20.GL_INVALID_OPERATION) {
                GLES20.glUniform4fv(colorUniformHandle, 1, backgroundColor, 0)
            } else {
                logEvent(
                    event = "Something went wrong while trying to inject a value for attribute u_Color...\n"
                )
                return
            }
        }

        textureCoordinateAttributeHandle =
            GLES20.glGetAttribLocation(programHandle, "a_TexCoordinate")
        logEvent(
            event = "textureCoordinateAttributeHandle: $textureCoordinateAttributeHandle\n"
        )

        if (textureCoordinateAttributeHandle != -1 && textureCoordinateAttributeHandle != GLES20.GL_INVALID_VALUE && textureCoordinateAttributeHandle != GLES20.GL_INVALID_OPERATION) {

            GLES20.glEnableVertexAttribArray(textureCoordinateAttributeHandle)

            glVertexAttribPointer(
                textureCoordinateAttributeHandle,
                TEXTURE_COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                textureCoordinateStride,
                textureCoordinateBuffer
            )

            //glDisableVertexAttribArray(textureCoordinateAttributeHandle)
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
            logEvent(
                event = "Something went wrong while trying to inject a value for attribute a_Position...\n"
            )
            return
        }

        textureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture")
        logEvent(
            event = "textureUniformHandle: $textureUniformHandle\n"
        )
        logEvent(
            event = "textureDataHandle: $textureDataHandle\n"
        )

        if (textureUniformHandle != -1 && textureUniformHandle != GLES20.GL_INVALID_VALUE && textureUniformHandle != GLES20.GL_INVALID_OPERATION) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle)
            GLES20.glUniform1i(textureUniformHandle, 0)
        }
    }
}