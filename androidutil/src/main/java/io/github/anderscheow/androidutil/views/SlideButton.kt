package io.github.anderscheow.androidutil.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar
import io.github.anderscheow.androidutil.R

class SlideButton @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : AppCompatSeekBar(context, attrs, defStyle) {

    private var mThumb: Drawable? = null
    private var listener: SlideButtonListener? = null
    private val orientation: Int

    var progressThreshold: Int = 90

    interface SlideButtonListener {
        fun handleSlide()

        fun fadeOutText(progress: Double)
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.slideButton)
        orientation = a.getInteger(R.styleable.slideButton_orientation, ORIENTATION_HORIZONTAL)
        a.recycle()
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (orientation == ORIENTATION_VERTICAL) {
            super.onMeasure(heightMeasureSpec, widthMeasureSpec)
            setMeasuredDimension(measuredHeight, measuredWidth)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onDraw(c: Canvas) {
        if (orientation == ORIENTATION_VERTICAL) {
            c.rotate(90f)
            c.translate(0f, (-width).toFloat())
        }
        super.onDraw(c)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (orientation == ORIENTATION_VERTICAL) {
            super.onSizeChanged(h, w, oldh, oldw)
        } else {
            super.onSizeChanged(w, h, oldw, oldh)
        }
    }

    override fun setThumb(thumb: Drawable) {
        super.setThumb(thumb)
        this.mThumb = thumb
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        if (orientation == ORIENTATION_HORIZONTAL) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (mThumb!!.bounds.contains(event.x.toInt(), event.y.toInt())) {
                    super.onTouchEvent(event)
                } else
                    return false
            } else if (event.action == MotionEvent.ACTION_UP) {
                progress = if (progress > progressThreshold) {
                    handleSlide()
                    100
                } else {
                    0
                }
            } else
                super.onTouchEvent(event)

            showProgress(progress.toDouble())
        } else {
            val i: Int
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        if (!mThumb!!.bounds.contains(event.y.toInt(), event.x.toInt())) {
                            return false
                        }
                    }
                    i = max - (max * event.y / height).toInt()
                    progress = 100 - i
                    onSizeChanged(width, height, 0, 0)
                    showProgress(progress.toDouble())
                }

                MotionEvent.ACTION_MOVE -> {
                    i = max - (max * event.y / height).toInt()
                    progress = 100 - i
                    onSizeChanged(width, height, 0, 0)
                    showProgress(progress.toDouble())
                }

                MotionEvent.ACTION_UP -> {
                    i = max - (max * event.y / height).toInt()
                    if (i < 30) {
                        handleSlide()
                    }
                    progress = 0
                    onSizeChanged(width, height, 0, 0)
                }

                MotionEvent.ACTION_CANCEL -> {
                }
            }
        }
        return true
    }

    fun resetProgress() {
        progress = 0
        showProgress(0.0)
    }

    private fun handleSlide() {
        listener?.handleSlide()
    }

    private fun showProgress(progress: Double) {
        listener?.fadeOutText(progress)
    }

    fun setSlideButtonListener(listener: SlideButtonListener) {
        this.listener = listener
    }

    companion object {

        const val ORIENTATION_HORIZONTAL = 0
        const val ORIENTATION_VERTICAL = 1
    }
}
