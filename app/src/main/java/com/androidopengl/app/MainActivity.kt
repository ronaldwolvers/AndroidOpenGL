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
const val DEFAULT_EYE_Z_F = 3F
const val DEFAULT_UP_Y_F = 1F

class MainActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: MyGLSurfaceView
    private lateinit var myRenderer: MyRenderer

    private lateinit var nearF: EditText
    private lateinit var farF: EditText
    private lateinit var eyeZF: EditText
    private lateinit var upYF: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        glSurfaceView = findViewById(R.id.gl_surface_view)

        if (glSurfaceView.getRenderer() == null) {
            myRenderer = MyRenderer()
            glSurfaceView.setRenderer(myRenderer)
        }

        glSurfaceView.renderMode = renderMode.renderMode

        open class AndroidOpenGLTextChanged(val operation: ((CharSequence?)-> Unit)): TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    operation.invoke(s)
                } catch (e: Exception) {
                    logError("Something went wrong: ${e.message}")
                    e.printStackTrace()
                }

                logEvent("Requesting render...")
                glSurfaceView.requestRender()
            }
        }

        fun EditText.addAndroidOpenGLTextChanged(operation: (CharSequence?) -> Unit) {
            this.addTextChangedListener(object: AndroidOpenGLTextChanged(operation){})
        }

        nearF = findViewById(R.id.edit_text_frustum_near_f)
        nearF.setText(DEFAULT_NEAR_F.toString())
        nearF.addAndroidOpenGLTextChanged {
            logEvent("onTextChanged() is being called on `nearF`...")
            val changedString = it.toString()
            myRenderer.frustumNearF = changedString.toFloat()
        }

        farF = findViewById(R.id.edit_text_frustum_far_f)
        farF.setText(DEFAULT_FAR_F.toString())
        farF.addAndroidOpenGLTextChanged {
            logEvent("onTextChanged() is being called on `farF`...")
            val changedString = it.toString()
            myRenderer.frustumFarF = changedString.toFloat()
        }

        eyeZF = findViewById(R.id.edit_text_eye_z_f)
        eyeZF.setText(DEFAULT_EYE_Z_F.toString())
        eyeZF.addAndroidOpenGLTextChanged {
            logEvent("onTextChanged() is being called on `eyeZF`...")
            val changedString = it.toString()
            myRenderer.eyeZF = changedString.toFloat()
        }

        upYF = findViewById(R.id.edit_text_up_y_f)
        upYF.setText(DEFAULT_UP_Y_F.toString())
        upYF.addAndroidOpenGLTextChanged {
            logEvent("onTextChanged() is being called on `upYF`...")
            val changedString = it.toString()
            myRenderer.upYF = changedString.toFloat()
        }
    }
}