package com.sdcgroup.speedmeter.notification

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Typeface
import com.sdcgroup.speedmeter.data.HumanSpeed

class IconGenerator {

    private val width = 72
    private val height = 72

    private val baseValueTextSize = 54f
    private val minValueTextSize = 26f
    private val baseUnitTextSize = 28f
    private val minUnitTextSize = 16f
    private val maxTextWidth = width * 0.94f
    private val lineGapPx = 1f

    private val valuePaint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
        textScaleX = 0.85f
        isAntiAlias = true
    }

    private val unitPaint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif-black", Typeface.BOLD)
        textScaleX = 0.9f
        isAntiAlias = true
    }

    private val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    private val canvas = Canvas(bitmap)
    private val valueBounds = Rect()
    private val unitBounds = Rect()

    fun generateIcon(speed: HumanSpeed): Bitmap {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        var valueTextSize = baseValueTextSize
        valuePaint.textSize = valueTextSize
        while (valuePaint.measureText(speed.speedValue) > maxTextWidth && valueTextSize > minValueTextSize) {
            valueTextSize -= 2f
            valuePaint.textSize = valueTextSize
        }

        var unitTextSize = baseUnitTextSize
        unitPaint.textSize = unitTextSize
        while (unitPaint.measureText(speed.speedUnit) > maxTextWidth && unitTextSize > minUnitTextSize) {
            unitTextSize -= 2f
            unitPaint.textSize = unitTextSize
        }

        valuePaint.getTextBounds(speed.speedValue, 0, speed.speedValue.length, valueBounds)
        unitPaint.getTextBounds(speed.speedUnit, 0, speed.speedUnit.length, unitBounds)

        val totalVisualHeight =
            valueBounds.height() + lineGapPx + unitBounds.height()

        val startY = (height - totalVisualHeight) / 2f

        val valueBaseline = startY - valueBounds.top
        val valueBottom = valueBaseline + valueBounds.bottom
        val unitBaseline = valueBottom + lineGapPx - unitBounds.top

        canvas.drawText(speed.speedValue, width / 2f, valueBaseline, valuePaint)
        canvas.drawText(speed.speedUnit, width / 2f, unitBaseline, unitPaint)

        return bitmap
    }
}
