package com.androidopengl.app

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import com.google.android.material.slider.Slider

@Suppress("unused")
enum class RenderMode(val renderMode: Int) {
    RENDERMODE_WHEN_DIRTY(GLSurfaceView.RENDERMODE_WHEN_DIRTY),
    RENDERMODE_CONTINUOUSLY(GLSurfaceView.RENDERMODE_CONTINUOUSLY)
}

val renderMode: RenderMode = RenderMode.RENDERMODE_CONTINUOUSLY

const val DEFAULT_ANGLE_IN_DEGREES_F = 10F
const val DEFAULT_NEAR_F = 3F
const val DEFAULT_FAR_F = 7F
const val DEFAULT_EYE_Y_F = 0F
const val DEFAULT_EYE_Z_F = 3F
const val DEFAULT_UP_Y_F = 1F
const val DEFAULT_TOP_Z = 0.0F
const val DEFAULT_SQUARES_Z = 0.0F

class MainActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: MyGLSurfaceView
    private lateinit var myRenderer: MyRenderer

    private lateinit var nearF: EditText
    private lateinit var farF: EditText
    private lateinit var eyeYF: EditText
    private lateinit var eyeYFSlider: Slider
    private lateinit var eyeZF: EditText
    private lateinit var upYF: EditText
    private lateinit var topZF: EditText
    private lateinit var topZFSlider: Slider
    private lateinit var squaresZF: EditText
    private lateinit var squaresZFSlider: Slider
    private lateinit var resetParametersButton: Button

    private fun resetParameters() {
        nearF.setText(DEFAULT_NEAR_F.toString())
        farF.setText(DEFAULT_FAR_F.toString())
        eyeYFSlider.value = DEFAULT_EYE_Y_F
        eyeZF.setText(DEFAULT_EYE_Z_F.toString())
        upYF.setText(DEFAULT_UP_Y_F.toString())
        topZFSlider.value = DEFAULT_TOP_Z
        squaresZFSlider.value = DEFAULT_SQUARES_Z
    }

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

        eyeYF = findViewById(R.id.edit_text_eye_y_f)
        eyeYF.setText(DEFAULT_EYE_Y_F.toString())
        eyeYF.addAndroidOpenGLTextChanged {
            logEvent("onTextChanged() is being called on `eyeYF`...")
            val changedString = it.toString()
            myRenderer.eyeYF = changedString.toFloat()
        }

        eyeYFSlider = findViewById(R.id.slider_eye_z_f)
        eyeYFSlider.value = DEFAULT_EYE_Y_F
        eyeYFSlider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            eyeYF.setText(String.format("%.1f", value))
        })

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

        topZF = findViewById(R.id.edit_text_top_z_f)
        topZF.setText(DEFAULT_TOP_Z.toString())
        topZF.addAndroidOpenGLTextChanged {
            logEvent("onTextChanged() is being called on `topZF`...")
            val changedString = it.toString()
            myRenderer.topZF = changedString.toFloat()
        }

        topZFSlider = findViewById(R.id.slider_top_z_f)
        topZFSlider.value = DEFAULT_TOP_Z
        topZFSlider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            topZF.setText(String.format("%.1f", value))
        })

        squaresZF = findViewById(R.id.edit_text_squares_z_f)
        squaresZF.setText(DEFAULT_SQUARES_Z.toString())
        squaresZF.addAndroidOpenGLTextChanged {
            logEvent("onTextChanged() is being called on `squaresZF`...")
            val changedString = it.toString()
            myRenderer.squaresZF = changedString.toFloat()
        }

        squaresZFSlider = findViewById(R.id.slider_squares_z_f)
        squaresZFSlider.value = DEFAULT_SQUARES_Z
        squaresZFSlider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            squaresZF.setText(String.format("%.1f", value))
        })

        resetParametersButton = findViewById(R.id.button_reset_parameters)
        resetParametersButton.setOnClickListener {
            resetParameters()
        }
    }
}