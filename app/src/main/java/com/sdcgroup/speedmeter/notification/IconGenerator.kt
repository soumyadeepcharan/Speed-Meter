package com.sdcgroup.speedmeter.notification

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Typeface
import com.sdcgroup.speedmeter.data.HumanSpeed

class IconGenerator {

    private val width = 72
    private val height = 96

    private val valuePaint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
        isAntiAlias = true
    }

    private val unitPaint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif-black", Typeface.BOLD)
        isAntiAlias = true
    }

    private val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    private val canvas = Canvas(bitmap)

    fun generateIcon(speed: HumanSpeed): Bitmap {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        val digitCount = speed.speedValue.length
        valuePaint.textSize = when {
            digitCount <= 2 -> 60f
            digitCount == 3 -> 48f
            else -> 38f
        }
        unitPaint.textSize = 28f

        canvas.drawText(speed.speedValue, width / 2f, 58f, valuePaint)
        canvas.drawText(speed.speedUnit, width / 2f, 90f, unitPaint)

        return bitmap
    }
}
