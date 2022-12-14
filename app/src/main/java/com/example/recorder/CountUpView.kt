package com.example.recorder

import android.content.Context
import android.icu.text.SymbolTable
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CountUpView(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var startTimestamp: Long = 0L

    private val countUpAction: Runnable = object : Runnable{
        override fun run() {
            val currentTimestamp = SystemClock.elapsedRealtime()

            val countTimeSeconds = ((currentTimestamp - startTimestamp)/1000L).toInt()
            updateCountTime(countTimeSeconds)

            handler?.postDelayed(this, 1000L)
        }
    }

    fun startCountUp(){
        startTimestamp = SystemClock.elapsedRealtime()
        handler?.post(countUpAction)
    }
    fun stopCountUp(){
        handler?.removeCallbacks(countUpAction)
    }
    private fun updateCountTime(CountTimeSecond: Int){
        val minute = CountTimeSecond / 60
        val second = CountTimeSecond % 60

        text = "%02d:%02d".format(minute,second)
    }
}