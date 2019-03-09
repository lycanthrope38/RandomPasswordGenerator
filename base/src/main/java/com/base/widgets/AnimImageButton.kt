package com.base.widgets

import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageButton
import com.base.R

/**
 * Created by tuan.lam on 3/12/2018.
 */
class AnimImageButton : ImageButton {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.animator.common_button_anim)
    }
}