package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private val rect = RectF()
    private val circleRect = RectF()
    private var topRect = RectF()
    private var progress: Float = 720f

    private var valueAnimator = ValueAnimator()

    private var buttonText = resources.getString(R.string.button_name)

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                isClickable = false
                buttonText = resources.getString(R.string.button_loading)
                valueAnimator.start()
                valueAnimator.addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Float
                    progress = animatedValue
                    invalidate()

                }
            }
            ButtonState.Completed -> {
                isClickable = true
                buttonText = resources.getString(R.string.button_download)
                valueAnimator.end()


            }
            ButtonState.Clicked -> {
                isClickable = true
                buttonText = resources.getString(R.string.button_download)
                valueAnimator.end()
            }
        }
    }


    init {
        isClickable = true

        //value animator
        valueAnimator = ValueAnimator.ofFloat(0f, 720f)
        valueAnimator.duration = 2000
//        valueAnimator.repeatCount = ValueAnimator.INFINITE
    }

    private val backgroundButtonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.colorPrimary)
    }

    private val paintButtonText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        typeface = Typeface.DEFAULT_BOLD
        color = context.getColor(R.color.white)
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.default_text_size)
    }

    private val progressBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorPrimaryDark)
        style = Paint.Style.FILL
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.colorAccent)

    }

    fun updateButtonState(state: ButtonState) {
        buttonState = state
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        rect.left = 0f
        rect.top = 0f
        rect.right = widthSize.toFloat()
        rect.bottom = heightSize.toFloat()

        topRect = rect
        //set the custom color
//        backgroundButtonPaint.setColor(context.getColor(R.color.colorPrimary))
//        paintButtonText.setColor(context.getColor(R.color.white))

        canvas?.drawRect(rect, backgroundButtonPaint)

        canvas?.drawText(
            buttonText,
            (widthSize / 2).toFloat(),
            (heightSize / 1.5).toFloat(),
            paintButtonText
        )

        if (progress == 720f) {

            topRect.right = 0f
        } else {
            circleRect.top = 0f + paintButtonText.descent()
            circleRect.left = rect.right - 200f
            circleRect.right = rect.right - 10f
            circleRect.bottom = (heightSize).toFloat() - paintButtonText.descent()

            canvas?.drawArc(circleRect, 360f, progress, true, circlePaint)

            topRect.right = widthSize * (progress / 720)
        }



        canvas?.drawRect(topRect, progressBarPaint)
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true
        postInvalidate()
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}