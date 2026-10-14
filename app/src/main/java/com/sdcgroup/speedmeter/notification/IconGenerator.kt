package com.sdcgroup.speedmeter.notification

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.sdcgroup.speedmeter.data.HumanSpeed

class IconGenerator {

    private val iconSpeedPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        textSize = 130f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
        textScaleX = 0.98f
        letterSpacing = -0.08f
    }

    private val iconUnitPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        textSize = 80f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
        textScaleX = 1f
        letterSpacing = -0.05f
    }

    fun generateIcon(speed: HumanSpeed): Bitmap {
        val bitmap = Bitmap.createBitmap(192, 192, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawText(speed.speedValue, 96f, 104f, iconSpeedPaint)
        canvas.drawText(speed.speedUnit, 96f, 190f, iconUnitPaint)
        return bitmap
    }
}
