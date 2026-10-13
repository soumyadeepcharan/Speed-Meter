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
    private val height = 72

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

    fun generateIcon(speed: HumanSpeed): Bitmap {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        valuePaint.textSize = 48f
        unitPaint.textSize = 24f

        val valueMetrics = valuePaint.fontMetrics
        val unitMetrics = unitPaint.fontMetrics

        val valueHeight = valueMetrics.descent - valueMetrics.ascent
        val unitHeight = unitMetrics.descent - unitMetrics.ascent
        val totalHeight = valueHeight + unitHeight

        val startY = (height - totalHeight) / 2f
        val valueBaseline = startY - valueMetrics.ascent
        val unitBaseline = startY + valueHeight - unitMetrics.ascent

        canvas.drawText(speed.speedValue, width / 2f, valueBaseline, valuePaint)
        canvas.drawText(speed.speedUnit, width / 2f, unitBaseline, unitPaint)

        return bitmap
    }
}
