package com.base.widgets

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.base.R
import com.base.utils.DisplayUtil
import com.base.utils.RippleUtil

class RippleFrameLayout : FrameLayout {

    companion object {

        private const val BASE_ON_LINEAR = true
        private const val SHOULD_SQUARE = false
        private const val PERCENT = 0

        private const val NORMAL_COLOR = Color.WHITE
        private const val IS_BACKGROUND = true
        private const val CORNER_RADIUS = 0
        private const val STROKE_WIDTH = 0
        private const val RIPPLE_COLOR = Color.DKGRAY
    }

    private var mMeasureBaseOnLinear = BASE_ON_LINEAR
    private var mShouldSquare = SHOULD_SQUARE
    private var mPercent = PERCENT

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        if (Build.VERSION.SDK_INT >= 23) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.RippleFrameLayout, defStyleAttr, 0)

            mMeasureBaseOnLinear = a.getBoolean(R.styleable.RippleFrameLayout_rfl_measure_linear, BASE_ON_LINEAR)
            mShouldSquare = a.getBoolean(R.styleable.RippleFrameLayout_rfl_should_square, SHOULD_SQUARE)
            mPercent = a.getInt(R.styleable.RippleFrameLayout_rfl_percent, PERCENT)

            val isBackground = a.getBoolean(R.styleable.RippleFrameLayout_rfl_is_background, IS_BACKGROUND)

            val cornerRadius = a.getDimensionPixelSize(R.styleable.RippleFrameLayout_rfl_corners, CORNER_RADIUS)
            val leftTopCornerRadius = a.getDimensionPixelSize(R.styleable.RippleFrameLayout_rfl_left_top_corner, CORNER_RADIUS)
            val rightTopCornerRadius = a.getDimensionPixelSize(R.styleable.RippleFrameLayout_rfl_right_top_corner, CORNER_RADIUS)
            val leftBottomCornerRadius = a.getDimensionPixelSize(R.styleable.RippleFrameLayout_rfl_left_bottom_corner, CORNER_RADIUS)
            val rightBottomCornerRadius = a.getDimensionPixelSize(R.styleable.RippleFrameLayout_rfl_right_bottom_corner, CORNER_RADIUS)
            val strokeWidth = a.getDimensionPixelSize(R.styleable.RippleFrameLayout_rfl_stroke_width, STROKE_WIDTH)
            val strokeColor = a.getColor(R.styleable.RippleFrameLayout_rfl_stroke_color, RIPPLE_COLOR)
            val rippleColor = a.getColor(R.styleable.RippleFrameLayout_rfl_ripple_color, RIPPLE_COLOR)
            val normalColor = a.getColor(R.styleable.RippleFrameLayout_rfl_normal_color, NORMAL_COLOR)

            a.recycle()

            if (isBackground) {
                background = if (strokeWidth == 0) {
                    RippleUtil.getRippleDrawable(normalColor, rippleColor, cornerRadius.toFloat(),
                            leftTopCornerRadius.toFloat(), rightTopCornerRadius.toFloat(),
                            leftBottomCornerRadius.toFloat(), rightBottomCornerRadius.toFloat())
                } else {
                    RippleUtil.getRippleStrokeDrawable(normalColor, rippleColor, cornerRadius.toFloat(), strokeWidth, strokeColor)
                }
            } else {
                foreground = if (strokeWidth == 0) {
                    RippleUtil.getRippleDrawable(Color.TRANSPARENT, rippleColor, cornerRadius.toFloat(),
                            leftTopCornerRadius.toFloat(), rightTopCornerRadius.toFloat(),
                            leftBottomCornerRadius.toFloat(), rightBottomCornerRadius.toFloat())
                } else {
                    RippleUtil.getRippleStrokeDrawable(Color.TRANSPARENT, rippleColor, cornerRadius.toFloat(), strokeWidth, strokeColor)
                }
            }
        }
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mPercent == 0) {
            if (mShouldSquare) {
                val size = if (mMeasureBaseOnLinear) widthMeasureSpec else heightMeasureSpec
                super.onMeasure(size, size)
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        } else {
            val screenSize = if (mMeasureBaseOnLinear)
                DisplayUtil.getDeviceWidth(context)
            else
                DisplayUtil.getDeviceHeight(context)
            val size = screenSize * mPercent / 100
            val measureSize = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY)
            if (mMeasureBaseOnLinear) {
                super.onMeasure(measureSize, if (mShouldSquare) measureSize else heightMeasureSpec)
            } else {
                super.onMeasure(if (mShouldSquare) measureSize else widthMeasureSpec, measureSize)
            }
        }
    }

}