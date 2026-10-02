package com.example.lightdetector

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View

class LightOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val detectedPoints = mutableListOf<PointF>()

    private val outerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.CYAN
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val innerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    fun updatePoints(points: List<PointF>) {
        detectedPoints.clear()
        detectedPoints.addAll(points)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (point in detectedPoints) {
            val x = point.x * width
            val y = point.y * height
            canvas.drawCircle(x, y, 16f, outerPaint)
            canvas.drawCircle(x, y, 7f, innerPaint)
        }
    }
}
