package com.androidopengl.app

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

@Suppress("unused")
enum class RenderMode(val renderMode: Int) {
    RENDERMODE_WHEN_DIRTY(GLSurfaceView.RENDERMODE_WHEN_DIRTY),
    RENDERMODE_CONTINUOUSLY(GLSurfaceView.RENDERMODE_CONTINUOUSLY)
}

val renderMode: RenderMode = RenderMode.RENDERMODE_WHEN_DIRTY

class MainActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: MyGLSurfaceView
    private lateinit var myRenderer: MyRenderer

    private lateinit var nearF: EditText
    private lateinit var farF: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        glSurfaceView = findViewById(R.id.gl_surface_view)

        if (glSurfaceView.getRenderer() == null) {
            myRenderer = MyRenderer()
            glSurfaceView.setRenderer(myRenderer)
        }

        glSurfaceView.renderMode = renderMode.renderMode

        nearF = findViewById(R.id.edit_text_frustum_near_f)
        nearF.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                logEvent("onTextChanged() is being called...")
                if (s is String) {
                    try {
                        myRenderer.frustumNearF = s.toFloat()
                        glSurfaceView.requestRender()
                    } catch (exception: Exception) {
                        exception.message?.let { logError(error = it) }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //TODO("Not yet implemented")
            }

        })
    }
}