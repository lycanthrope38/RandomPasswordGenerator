package com.base.widgets

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.base.R

/**
 * Created by vophamtuananh on 3/16/18.
 */
class SquareImageView : AppCompatImageView {

    companion object {
        private const val DEFAULT_SQUARE_FOLLOW_HEIGHT = false
    }

    private var mSquareFollowHeight = DEFAULT_SQUARE_FOLLOW_HEIGHT


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView, defStyleAttr, 0)

        mSquareFollowHeight = a.getBoolean(R.styleable.SquareImageView_siv_follow_height, DEFAULT_SQUARE_FOLLOW_HEIGHT)

        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val should = if (mSquareFollowHeight) heightMeasureSpec else widthMeasureSpec
        super.onMeasure(should, should)
    }
}