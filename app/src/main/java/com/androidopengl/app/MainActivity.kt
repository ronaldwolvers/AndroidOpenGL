package com.androidopengl.app

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

@Suppress("Unused")
enum class RenderMode(val renderMode: Int) {
    RENDERMODE_WHEN_DIRTY(GLSurfaceView.RENDERMODE_WHEN_DIRTY),
    RENDERMODE_CONTINUOUSLY(GLSurfaceView.RENDERMODE_CONTINUOUSLY)
}

val renderMode: RenderMode = RenderMode.RENDERMODE_CONTINUOUSLY

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}