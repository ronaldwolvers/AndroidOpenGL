package com.androidopengl.app.opengl.square

import android.content.Context
import com.androidopengl.app.opengl.triangle.TriangleOpenGLObject

class SquareOpenGLObject (
    private val context: Context? = null,
    private val color: FloatArray,
    val coordinates: FloatArray
) {

    private val mTriangle1: TriangleOpenGLObject =
        TriangleOpenGLObject(context, color, coordinates.sliceArray(0..8))
    private val mTriangle2: TriangleOpenGLObject =
        TriangleOpenGLObject(context, color, coordinates.sliceArray(3..11))

    fun draw(mvpMatrix: FloatArray? = null) {
        mTriangle1.draw(mvpMatrix)
        mTriangle2.draw(mvpMatrix)
    }
}