package com.androidopengl.app.opengl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import com.androidopengl.app.logEvent
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

const val COORDINATES_PER_VERTEX = 3
const val TEXTURE_COORDINATES_PER_VERTEX = 2

abstract class OpenGLObject(
    private val coordinates: FloatArray,
    private val context: Context? = null
) {

    fun reinitializeVertexBuffer() {
        vertexBuffer.position(0)
    }

    private fun initializeVertexBuffer(): FloatBuffer {

        logEvent("initializeVertexBuffer() is being called...")

        //4 bytes per float.
        return ByteBuffer.allocateDirect(coordinates.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(coordinates)
                position(0)
            }
        }
    }

    val vertexStride: Int = COORDINATES_PER_VERTEX * 4
    var vertexBuffer: FloatBuffer = initializeVertexBuffer()

    val vertexCount: Int = this.coordinates.size / COORDINATES_PER_VERTEX

    abstract fun createProgram()

    private val textureCoordinates: FloatArray = floatArrayOf(
        0.0f, 0.0f,     //bottom left
        1.0f, 0.0f,     //bottom right
        0.0f, 1.0f,     //top left
        1.0f, 1.0f,     //top right
    )
    val textureCoordinateStride: Int = TEXTURE_COORDINATES_PER_VERTEX * 4
    var textureCoordinateBuffer: FloatBuffer =
        //4 bytes per float.
        ByteBuffer.allocateDirect(textureCoordinates.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(textureCoordinates)
                position(0)
            }
        }

    fun loadTexture(textureResourceId: Int): Int {
        val textureHandles = IntArray(1)
        GLES20.glGenTextures(1, textureHandles, 0)
        if (context != null && textureHandles[0] != 0) {
            val bitmapFactoryOptions = BitmapFactory.Options()
            bitmapFactoryOptions.inScaled = false
            val bitmap = BitmapFactory.decodeResource(
                context.resources, textureResourceId, bitmapFactoryOptions
            )

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0])

            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST
            )

            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST
            )

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

            bitmap.recycle()
        }
        return textureHandles[0]
    }

    fun loadTextureFromBitmap(bitmap: Bitmap): Int {

        logEvent("Loading texture from a bitmap...")

        val textureHandles = IntArray(1)
        GLES20.glGenTextures(1, textureHandles, 0)

        for (textureHandle in textureHandles) {
            logEvent(
                event = "textureHandle: $textureHandle"
            )
        }

        if (textureHandles[0] != 0) {

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0])

            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST
            )

            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST
            )

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

            bitmap.recycle()
        }
        return textureHandles[0]
    }
}