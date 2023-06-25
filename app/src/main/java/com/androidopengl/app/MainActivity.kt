package com.androidopengl.app

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

enum class RenderMode(val renderMode: Int) {
    @Suppress("unused")
    RENDERMODE_WHEN_DIRTY(GLSurfaceView.RENDERMODE_WHEN_DIRTY),
    RENDERMODE_CONTINUOUSLY(GLSurfaceView.RENDERMODE_CONTINUOUSLY)
}

val renderMode: RenderMode = RenderMode.RENDERMODE_CONTINUOUSLY

class MainActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: MyGLSurfaceView
    private lateinit var myRenderer: MyRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        glSurfaceView = findViewById(R.id.gl_surface_view)

        if (glSurfaceView.getRenderer() == null) {
            myRenderer = MyRenderer()
            glSurfaceView.setRenderer(myRenderer)
        }

        glSurfaceView.renderMode = renderMode.renderMode
    }
}