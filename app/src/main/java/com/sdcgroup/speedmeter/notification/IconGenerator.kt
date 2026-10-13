package com.sdcgroup.speedmeter.notification

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Typeface
import com.sdcgroup.speedmeter.data.HumanSpeed

class IconGenerator {

    private val size = 96

    private val valuePaint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
        isAntiAlias = true
    }

    private val unitPaint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }

    private val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    private val canvas = Canvas(bitmap)

    fun generateIcon(speed: HumanSpeed): Bitmap {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        val digitCount = speed.speedValue.length
        valuePaint.textSize = when {
            digitCount <= 2 -> 46f
            digitCount == 3 -> 38f
            else -> 30f
        }
        unitPaint.textSize = 20f

        canvas.drawText(speed.speedValue, size / 2f, 54f, valuePaint)
        canvas.drawText(speed.speedUnit, size / 2f, 84f, unitPaint)

        return bitmap
    }
}
