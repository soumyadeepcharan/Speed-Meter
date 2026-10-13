package com.sdcgroup.speedmeter.notification

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Typeface
import com.sdcgroup.speedmeter.data.HumanSpeed

class IconGenerator {

    private val iconSpeedPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        textSize = 65f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
        textScaleX = 0.98f
        letterSpacing = -0.08f
    }

    private val iconUnitPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        textSize = 40f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
        textScaleX = 1f
        letterSpacing = -0.05f
    }

    private val bitmap = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888)
    private val canvas = Canvas(bitmap)

    fun generateIcon(speed: HumanSpeed): Bitmap {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        canvas.drawText(speed.speedValue, 48f, 52f, iconSpeedPaint)
        canvas.drawText(speed.speedUnit, 48f, 95f, iconUnitPaint)
        return bitmap
    }
}
