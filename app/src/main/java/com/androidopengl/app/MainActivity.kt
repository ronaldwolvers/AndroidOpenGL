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

val renderMode: RenderMode = RenderMode.RENDERMODE_CONTINUOUSLY

const val DEFAULT_ANGLE_IN_DEGREES_F = 10F
const val DEFAULT_NEAR_F = 3F
const val DEFAULT_FAR_F = 7F

class MainActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: MyGLSurfaceView
    private lateinit var myRenderer: MyRenderer

    private lateinit var nearF: EditText
    private lateinit var farF: EditText
    private lateinit var eyeZ: EditText
    private lateinit var upY: EditText

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

                } catch (exception: Exception) {
                    logError("Something went wrong: ${exception.message}")
                    exception.printStackTrace()
                }

                logEvent("Requesting render...")
                glSurfaceView.requestRender()

            }
        })

        farF = findViewById(R.id.edit_text_frustum_far_f)
        farF.setText(DEFAULT_FAR_F.toString())
        farF.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                logEvent("onTextChanged() is being called on `farF`...")
                val changedString = s.toString()
                try {
                    myRenderer.frustumFarF = changedString.toFloat()

                } catch (exception: Exception) {
                    logError("Something went wrong: ${exception.message}")
                    exception.printStackTrace()
                }

                logEvent("Requesting render...")
                glSurfaceView.requestRender()
            }

        })


    }
}