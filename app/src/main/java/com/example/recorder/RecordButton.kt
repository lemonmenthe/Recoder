package com.example.recorder

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/* https://developer.android.com/training/custom-views/create-view */

class RecordButton(
    context: Context,
    attrs: AttributeSet
) : AppCompatImageView(context, attrs) {
    fun updateIconWithState(state: State) {
        when (state) {
            State.BEFORE_RECODING -> {
                setImageResource(R.drawable.ic_record)
            }
            State.ON_RECODING -> {
                setImageResource(R.drawable.ic_stop)
            }
            State.AFTER_RECODING -> {
                setImageResource(R.drawable.ic_play)
            }
            State.ON_PLAYING -> {
                setImageResource(R.drawable.ic_pause)
            }

        }
    }
}