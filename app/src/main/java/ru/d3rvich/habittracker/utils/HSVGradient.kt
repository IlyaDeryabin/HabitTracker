package ru.d3rvich.habittracker.utils

import android.graphics.*
import android.graphics.drawable.Drawable

class HSVGradient : Drawable() {

    private val paint = Paint()
    private val colors = IntArray(361)

    init {
        val hsv = FloatArray(3)
        hsv[1] = 1f
        hsv[2] = 1f
        for (i in 0 until 360) {
            hsv[0] = i.toFloat()
            colors[i] = Color.HSVToColor(hsv)
        }
        colors[360] = colors.first()
    }

    fun findHuePositionByColor(color: Int): Int {
        val index = colors.indexOf(color)
        check(index != -1) { "Not found" }
        return index
    }

    fun getColorAt(index: Int): Int {
        return colors[index]
    }

    fun getRGBColorAt(index: Int): Array<Int> {
        val color = colors[index]
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return arrayOf(red, green, blue)
    }

    fun getHSVColorAt(index: Int): FloatArray {
        val color = colors[index]
        val hsv = FloatArray(3)
        hsv[1] = 1f
        hsv[2] = 1f
        Color.colorToHSV(color, hsv)
        return hsv
    }

    override fun draw(canvas: Canvas) {
        val width = bounds.width().toFloat()
        val height = bounds.height().toFloat()

        val gradient = LinearGradient(0f, 0f, width, height, colors, null, Shader.TileMode.MIRROR)
        paint.isDither = true
        paint.shader = gradient

        canvas.drawPaint(paint)
    }

    override fun setAlpha(p0: Int) {
    }

    override fun setColorFilter(p0: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }
}