package com.androidopengl.app

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.androidopengl.app.opengl.triangle.TriangleOpenGLObject
import com.androidopengl.app.utils.colorToFloatArray
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLSurfaceView : GLSurfaceView {

    private var context: Context? = null

    constructor(context: Context) : super(context) {
        this.context = context
        logEvent("MyGLSurfaceView(context) is being called...")
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.context = context
        logEvent("MyGLSurfaceView(context, attrs) is being called...")
    }

    private var renderer: Renderer? = null

    override fun setRenderer(renderer: Renderer?) {

        renderer?.let {
            this.renderer = it
            if (renderer is MyRenderer) {
                renderer.context = context
            }
        }

        super.setRenderer(renderer)
    }

    fun getRenderer(): Renderer? {
        return renderer
    }

    init {

        logEvent("MyGLSurfaceView.init is being called...")

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)
    }

    private val TOUCH_SCALE_FACTOR: Float = 180.0f / 320.0f

    private var previousX: Float = 0f
    private var previousY: Float = 0f

    override fun onTouchEvent(e: MotionEvent): Boolean {

        logEvent( "MyGLSurfaceView.onTouchEvent() is being called...")

        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        val x: Float = e.x
        val y: Float = e.y

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {

                var dx: Float = x - previousX
                var dy: Float = y - previousY

                // reverse direction of rotation above the mid-line
                if (y > height / 2) {
                    dx *= -1
                }

                // reverse direction of rotation to left of the mid-line
                if (x < width / 2) {
                    dy *= -1
                }
                if (renderer is MyRenderer) {
                    (renderer as MyRenderer).angle += (dx + dy) * TOUCH_SCALE_FACTOR
                }
                requestRender()
            }
        }

        previousX = x
        previousY = y
        return true
    }

}

open class MyRenderer : GLSurfaceView.Renderer {

    @Volatile
    var angle: Float = DEFAULT_ANGLE_IN_DEGREES_F
        set(value) {
            logEvent("`angle` in MyRenderer is being set to: $value")
            field = value
        }

    var context: Context? = null

    private lateinit var mTriangle: Triangle
    private lateinit var mTriangle2: Triangle
    private lateinit var mTriangle3: Triangle
    //Triangles 5, 6 and 7 will replace the original triangles.
    private var mTriangle5: TriangleOpenGLObject? = null
    private var mTriangle6: TriangleOpenGLObject?= null
    private var mTriangle7: TriangleOpenGLObject? = null
    private var mTriangle4: TriangleOpenGLObject? = null

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {

        logEvent("onSurfaceCreated() is being called...")

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        mTriangle = Triangle(useMvpMatrix = true, color = floatArrayOf(0.1F, 0.2f, 0.23f, 0.5f))
        mTriangle2 = Triangle(useMvpMatrix = false)
        mTriangle3 = Triangle(
            useMvpMatrix = true, color = floatArrayOf(1.0F, 0.0F, 0.0F, 0.5F),
            coords =
            floatArrayOf(
                0.0f, 0.3008459f, 0.0f,         // top
                -0.3f, -0.111004243f, 0.0f,     // bottom left
                0.3f, -0.111004243f, 0.0f       // bottom right
            )
        )
        context?.let {
            mTriangle5 = TriangleOpenGLObject(it, ContextCompat.getColor(it, R.color.purple_200).colorToFloatArray(), floatArrayOf(
                0.0f, 0.62f, 0.0f,              // top
                -0.5f, -0.31f, 0.0f,            // bottom left
                0.5f, -0.31f, 0.0f              // bottom right
            ))
            mTriangle6 = TriangleOpenGLObject(it, ContextCompat.getColor(it, R.color.purple_500).colorToFloatArray(), floatArrayOf(
                0.0f, 0.62f, 0.0f,              // top
                -0.5f, -0.31f, 0.0f,            // bottom left
                0.5f, -0.31f, 0.0f              // bottom right
            ))
            mTriangle7 = TriangleOpenGLObject(it, ContextCompat.getColor(it, R.color.teal_700).colorToFloatArray(), floatArrayOf(
                0.0f, 0.3f, 0.0f,               // top
                -0.3f, -0.11f, 0.0f,            // bottom left
                0.3f, -0.11f, 0.0f              // bottom right
                ))
            mTriangle4 = TriangleOpenGLObject(it, ContextCompat.getColor(it, R.color.teal_200).colorToFloatArray(), floatArrayOf(
                0.5f, 0.5f, 0.0f,               // top
                0.4f, 0.4f, 0.0f,               // bottom left
                0.5f, 0.4f, 0.0f                // bottom right
            ))
        }
    }

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)
    private val rotationMatrix2 = FloatArray(16)

    var frustumNearF: Float? = null
        set(value) {
            logEvent("frustumNearF is being written to in MyRenderer.\n" +
                    "frustumNearF: $value")
            field = value
        }

    var frustumFarF: Float? = null
        set(value) {
            logEvent("frustumFarF is being written to in MyRenderer.\n" +
                    "frustumFarF: $value")
            field = value
        }

    private var ratio: Float? = null

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {

        logEvent("onSurfaceChanged() is being called...")

        GLES20.glViewport(0, 0, width, height)

        if (height != 0) {
            ratio = width.toFloat() / height.toFloat()
        }
    }

    override fun onDrawFrame(gl: GL10) {

        logEvent( "onDrawFrame() is being called...")

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        if (ratio != null) {
            try {
                Matrix.frustumM(
                    projectionMatrix,
                    0,
                    -ratio!!,
                    ratio!!,
                    -1f,
                    1f,
                    frustumNearF ?: DEFAULT_NEAR_F,
                    frustumFarF ?: DEFAULT_FAR_F
                )
            } catch (e: Exception) {
                logError("Something went wrong while calling Matrix.frustumM(): ${e.message}")
                e.printStackTrace()
            }
        } else {
            logError("Calculated `ratio` is null...")
        }

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Create a rotation transformation for the triangle
        val time = SystemClock.uptimeMillis() % 4000L
        val angleInTime = 0.090f * time.toInt()

        logEvent("Creating a rotation matrix...\n" +
                    "time: $time\n" +
                    "angle: $angleInTime"
        )

        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)

        Matrix.setRotateM(rotationMatrix2, 0, angleInTime, 0f, 0f, -1.0f)

        val scratch = FloatArray(16)
        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)

        val scratch2 = FloatArray(16)
        Matrix.multiplyMM(scratch2, 0, vPMatrix, 0, rotationMatrix2, 0)

//        mTriangle2.draw()
//        mTriangle.draw(scratch)
//        mTriangle3.draw(scratch2)
        mTriangle5?.draw()
        mTriangle6?.draw(scratch)
        mTriangle7?.draw(scratch2)
        mTriangle4?.draw(vPMatrix)
    }
}