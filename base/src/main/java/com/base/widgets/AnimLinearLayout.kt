package com.base.widgets

import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat

/**
 * Created by tuan.lam on 3/12/2018.
 */
class AnimLinearLayout : LinearLayoutCompat {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.stateListAnimator = AnimatorInflater.loadStateListAnimator(context, com.base.R.animator.common_button_anim)
    }

}