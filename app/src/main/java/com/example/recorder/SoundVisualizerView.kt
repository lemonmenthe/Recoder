package com.example.recorder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class SoundVisualizerView(context: Context,attrs: AttributeSet?= null): View(context, attrs) {

    var onRequestCurrentAmpLitude: (()->Int)? = null

    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {  //안티엘리어싱
        color = context.getColor(R.color.purple_500)  //색지정
        strokeCap = Paint.Cap.ROUND //끝부분처리
        strokeWidth = LINE_WIDTH

    }
    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0
    private var drawingAmplitudes: List<Int> = emptyList()
    private var isReplaying: Boolean = false
    private var replayingPosition: Int = 0


    private val visualizeRepatAction: Runnable = object : Runnable {
        override fun run() {
            if(!isReplaying) {
                val currentAmplitute = onRequestCurrentAmpLitude?.invoke() ?: 0
                drawingAmplitudes = listOf(currentAmplitute) + drawingAmplitudes
            }else{
                replayingPosition++
            }
            invalidate()

            handler?.postDelayed(this,ACTION_INTERVAL) //20ms기준
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingHeight = h
        drawingWidth = w
        //새로운 값으로 변경
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)  //표현하는 부분

        canvas ?: return  // null일경우 종료

        // view 위치
        val centerY = drawingHeight / 2f
        //시작부분
        var offsetX = drawingWidth.toFloat()


        drawingAmplitudes
            .let { amplitudes ->
            if(isReplaying){
                amplitudes.takeLast(replayingPosition)
            }else{
                amplitudes
             }
            }
            .forEach{ amplitude ->
            val lineLength = amplitude / MAX_AMPLITUTE * drawingHeight * LINE_HEIGHT // 증폭값 조절

            offsetX -= LINE_SPACE
            if(offsetX <0) return@forEach //뷰보다 크면 중지

            canvas.drawLine(
                offsetX,
                centerY - lineLength / 2F,
                offsetX,
                centerY + lineLength / 2F,
                amplitudePaint
            )
        }



    }

    // 시작 종료
     fun startVisualizing(isReplaying: Boolean){
        this.isReplaying = isReplaying  //녹음중인지 확인
        handler.post(visualizeRepatAction)
    }
     fun stopViualizing(){
        handler.removeCallbacks(visualizeRepatAction)
    }


    companion object {
        private const val  LINE_WIDTH = 10F
        private const val  LINE_SPACE = 15F
        private const val  MAX_AMPLITUTE = Short.MAX_VALUE.toFloat()
        private const val ACTION_INTERVAL = 20L
        private const val LINE_HEIGHT = 0.6F
    }

}