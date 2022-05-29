package com.androidopengl.app

import android.opengl.GLES20
import android.util.Log

fun loadShader(type: Int, shaderCode: String): Int {

    Log.d(
        "AndroidOpenGL", "loadShader() is being called...\n" +
                "type: ${"%x".format(type).uppercase()}"
    )

    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
    return GLES20.glCreateShader(type).also { shader ->

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
    }
}