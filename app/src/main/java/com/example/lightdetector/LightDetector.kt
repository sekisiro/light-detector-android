package com.example.lightdetector

import android.graphics.PointF
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Moments
import org.opencv.imgproc.Imgproc
import kotlin.math.abs

class LightDetector(
    private var brightnessThreshold: Double = 180.0,
    private var minArea: Double = 30.0,
    private var maxLights: Int = 20
) {

    fun setThreshold(value: Int) {
        brightnessThreshold = value.toDouble()
    }

    fun setMinArea(value: Int) {
        minArea = value.toDouble()
    }

    fun detect(gray: Mat): List<PointF> {
        if (gray.empty()) return emptyList()

        val binary = Mat()
        Imgproc.threshold(gray, binary, brightnessThreshold, 255.0, Imgproc.THRESH_BINARY)

        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)

        val points = ArrayList<PointF>()
        for (contour in contours) {
            val area = Imgproc.contourArea(contour)
            if (area < minArea) continue

            val moments: Moments = Imgproc.moments(contour)
            val m00 = moments.m00
            if (abs(m00) < 1e-6) continue

            val centerX = (moments.m10 / m00).toFloat()
            val centerY = (moments.m01 / m00).toFloat()
            val normalizedX = centerX / gray.cols().toFloat()
            val normalizedY = centerY / gray.rows().toFloat()

            if (normalizedX in 0.0f..1.0f && normalizedY in 0.0f..1.0f) {
                points.add(PointF(normalizedX, normalizedY))
            }
        }

        return points
            .sortedBy { it.x }
            .take(maxLights)
    }
}
