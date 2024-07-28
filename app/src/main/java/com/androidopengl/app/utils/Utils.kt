package com.androidopengl.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import kotlin.math.roundToInt

fun createBitmapFromCharSequence(
    charSequence: CharSequence,
    heightInPx: Int = 100,
    widthInPx: Int = 100,
    backgroundColor: FloatArray? = null,
    textSize: Float = 70F,
    textMarginBottom: Float = 30F
): Bitmap {
    val bitmap = Bitmap.createBitmap(widthInPx, heightInPx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val textPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        this.textSize = textSize
        textAlign = Paint.Align.CENTER
    }
    if (backgroundColor == null) {
        canvas.drawARGB(0, 255, 255, 255)
    } else {
        canvas.drawARGB(
            backgroundColor.a.toColorInt(),
            backgroundColor.r.toColorInt(),
            backgroundColor.g.toColorInt(),
            backgroundColor.b.toColorInt()
        )
    }
    canvas.drawText(
        charSequence,
        0,
        charSequence.length,
        (widthInPx / 2).toFloat(),
        (heightInPx / 2).toFloat() + textMarginBottom,
        textPaint
    )
    return bitmap
}

@Suppress("unused")
fun Int.dp2px(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, toFloat(), context.resources.displayMetrics
    ).toInt()
}

@Suppress("unused")
fun Int.px2dp(context: Context): Int {
    return (this / context.resources.displayMetrics.density).roundToInt()
}

fun Int.colorToFloatArray(): FloatArray {

    val A = (this shr 24 and 0xff) / 255.0F
    val R = (this shr 16 and 0xff) / 255.0F
    val G = (this shr 8 and 0xff) / 255.0F
    val B = (this and 0xff) / 255.0F

    return floatArrayOf(R, G, B, A)
}

val FloatArray.r: Float
    get() = this[0]

val FloatArray.g: Float
    get() = this[1]

val FloatArray.b: Float
    get() = this[2]

val FloatArray.a: Float
    get() = this[3]

fun Float.toColorInt(): Int {
    return (this * 255.0F).toInt()
}

const val BUFFER_SIZE: Int = 1024

@Throws(IOException::class)
fun convertInputStreamToString(inputStream: InputStream): String {
    val buffer = CharArray(BUFFER_SIZE)
    val out = StringBuilder()
    val `in`: Reader = InputStreamReader(inputStream)
    var charsRead: Int
    inputStream.use {
        while (`in`.read(buffer, 0, buffer.size).also { charsRead = it } > 0) {
            out.appendRange(buffer, 0, charsRead)
        }
    }

    return out.toString()
}