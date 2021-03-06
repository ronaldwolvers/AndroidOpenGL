package com.androidopengl.app

import android.content.Context
import android.opengl.*
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLSurfaceView : GLSurfaceView {

    constructor(context: Context) : super(context) {
        Log.d("AndroidOpenGL", "MyGLSurfaceView(context) is being called...")
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        Log.d("AndroidOpenGL", "MyGLSurfaceView(context, attrs) is being called...")
    }

    private var renderer: Renderer? = null

    override fun setRenderer(renderer: Renderer?) {

        renderer?.let {
            this.renderer = it
        }

        super.setRenderer(renderer)
    }

    fun getRenderer(): Renderer? {
        return renderer
    }

    init {

        Log.d("AndroidOpenGL", "MyGLSurfaceView.init is being called...")

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)
    }

    private val TOUCH_SCALE_FACTOR: Float = 180.0f / 320.0f

    private var previousX: Float = 0f
    private var previousY: Float = 0f

    override fun onTouchEvent(e: MotionEvent): Boolean {

        Log.d("AndroidOpenGL", "MyGLSurfaceView.onTouchEvent() is being called...")

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
    var angle: Float = 0f
        set(value) {
            Log.d("AndroidOpenGL", "angle is being set to: $value")
            field = value
        }

    private lateinit var mTriangle: Triangle
    private lateinit var mTriangle2: Triangle
    private lateinit var mTriangle3: Triangle

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {

        Log.d("AndroidOpenGL", "onSurfaceCreated() is being called...")

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
    }

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)
    private val rotationMatrix2 = FloatArray(16)

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {

        Log.d("AndroidOpenGL", "onSurfaceChanged() is being called...")

        GLES20.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    override fun onDrawFrame(gl: GL10) {

        Log.d("AndroidOpenGL", "onDrawFrame() is being called...")

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Create a rotation transformation for the triangle
        val time = SystemClock.uptimeMillis() % 4000L
        val angleInTime = 0.090f * time.toInt()

        Log.d(
            "AndroidOpenGL", "creating a rotation matrix...\n" +
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

        mTriangle2.draw()
        mTriangle.draw(scratch)
        mTriangle3.draw(scratch2)
    }
}