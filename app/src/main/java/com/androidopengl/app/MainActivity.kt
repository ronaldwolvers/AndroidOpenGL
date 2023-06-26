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

const val DEFAULT_NEAR_F = 3F
const val DEFAULT_FAR_F = 7F

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
        nearF.setText(DEFAULT_NEAR_F.toString())
        nearF.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                logEvent("onTextChanged() is being called on `nearF`...")
                val changedString = s.toString()
                try {
                    myRenderer.frustumNearF = changedString.toFloat()
                    logEvent("Requesting render...")
                    glSurfaceView.requestRender()
                } catch (exception: Exception) {
                    logError("Something went wrong: ${exception.message}")
                    exception.printStackTrace()
                }
            }
        })

        farF = findViewById(R.id.edit_text_frustum_fram_f)
        farF.setText(DEFAULT_FAR_F.toString())
        farF.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                logEvent("onTextChanged() is being called on `farF`...")
                val changedString = s.toString()
                try {
                    myRenderer.frustumFarF = changedString.toFloat()
                    logEvent("Requesting render...")
                    glSurfaceView.requestRender()
                } catch (exception: Exception) {
                    logError("Something went wrong: ${exception.message}")
                    exception.printStackTrace()
                }
            }

        })
    }
}