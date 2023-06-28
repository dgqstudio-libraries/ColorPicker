package com.dgqstudio.colorpicker

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap

class ColorPicker @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    // Views
    private var colorView: View
    private var colorPickedLocator: LinearLayout
    private var colorPickedLocatorImage: ImageView
    private var selectedColorCardView: CardView
    private var brightnessSeekBar: SeekBar
    private var redSeekBar: SeekBar
    private var greenSeekBar: SeekBar
    private var blueSeekBar: SeekBar

    // Attrs
    private var initialColor: Int
    private var showColorPickedPointer: Boolean

    // Variables
    val colorViewCornerRadius: Float = 14f
    private var colorPickedPointerAnimator: ObjectAnimator? = null

    private var brightness: Int = 255 // 255: White, 0: Black

    private var red: Int = 0
    private var green: Int = 0
    private var blue: Int = 0

    private var colorRGB: Int = Color.rgb(red, green, blue)
    private var colorBrightness: Int = Color.rgb(brightness, brightness, brightness)

    companion object {
        var selectedColor: Int = 0
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.colorpicker, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ColorPicker,
            0,
            0
        ).apply {
            try {
                // Views
                colorView = findViewById(R.id.colorView)
                colorPickedLocator = findViewById(R.id.colorPickedPointer)
                colorPickedLocatorImage = findViewById(R.id.colorPickedPointerImage)
                selectedColorCardView = findViewById(R.id.selectedColorCardView)
                brightnessSeekBar = findViewById(R.id.brightnessSeekBar)
                redSeekBar = findViewById(R.id.redSeekBar)
                greenSeekBar = findViewById(R.id.greenSeekBar)
                blueSeekBar = findViewById(R.id.blueSeekBar)

                // Attrs
                initialColor =
                    getColor(R.styleable.ColorPicker_initialColor, Color.WHITE)
                showColorPickedPointer =
                    getBoolean(R.styleable.ColorPicker_showColorPickedPointer, true)

                // Variables
                selectedColor = initialColor

                init()
                initBinding()
            } finally {
                recycle()
            }
        }
    }

    private fun init() {
        setSelectedColor(selectedColor)

        setupBrightnessSeekBar()

        setupRedSeekBar()
        setupGreenSeekBar()
        setupBlueSeekBar()

        setGradientDrawable()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initBinding() {
        colorView.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                ACTION_DOWN -> {
                    setColorPickedPointerVisibility(visible = true)
                    colorViewTouched(
                        x = motionEvent.x.toInt(),
                        y = motionEvent.y.toInt()
                    )
                }

                ACTION_MOVE -> {
                    colorViewTouched(
                        x = motionEvent.x.toInt(),
                        y = motionEvent.y.toInt()
                    )
                }

                ACTION_UP -> {
                    setColorPickedPointerVisibility(visible = false)
                }
            }

            true
        }

        brightnessSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                brightness = p1

                colorBrightness = Color.rgb(brightness, brightness, brightness)
                setGradientDrawable()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        redSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                red = p1

                colorRGB = Color.rgb(red, green, blue)
                setGradientDrawable()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        greenSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                green = p1

                colorRGB = Color.rgb(red, green, blue)
                setGradientDrawable()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        blueSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                blue = p1

                colorRGB = Color.rgb(red, green, blue)
                setGradientDrawable()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun setColorPickedPointerVisibility(visible: Boolean) {
        if (showColorPickedPointer) {
            val animationDuration: Long = 300

            if (visible) {
                colorPickedPointerAnimator?.cancel()
                colorPickedLocator.alpha = 0f
                colorPickedLocator.visibility = View.VISIBLE

                colorPickedPointerAnimator =
                    ObjectAnimator.ofFloat(colorPickedLocator, "alpha", 0f, 1f).apply {
                        duration = animationDuration
                    }

                colorPickedPointerAnimator?.start()
            } else {
                colorPickedPointerAnimator =
                    ObjectAnimator.ofFloat(colorPickedLocator, "alpha", 1f, 0f).apply {
                        duration = animationDuration
                        addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                colorPickedLocator.visibility = View.GONE
                            }
                        })
                    }

                colorPickedPointerAnimator?.start()
            }
        }
    }

    private fun colorViewTouched(x: Int, y: Int) {
        val bitmap = colorView.background.toBitmap(
            width = colorView.width,
            height = colorView.height
        )

        val limitTop: Int = 0 + colorViewCornerRadius.toInt()
        val limitLeft: Int = 0 + colorViewCornerRadius.toInt()
        val limitRight: Int = bitmap.width - (colorPickedLocator.width / 2)
        val limitBottom: Int = bitmap.height - (colorPickedLocator.height / 2)

        val touchPositionIsOnBitmap: Boolean =
            x > limitLeft && y > limitTop && x < limitRight && y < limitBottom

        if (touchPositionIsOnBitmap) {
            val pixel = bitmap.getPixel(x, y)

            setSelectedColor(pixel)
            moveColorPickedPointer(
                x = x,
                y = y
            )
        }
    }

    private fun moveColorPickedPointer(x: Int, y: Int) {
        if (showColorPickedPointer) {
            colorPickedLocatorImage.setColorFilter(selectedColor)

            val params = colorPickedLocator.layoutParams as LayoutParams
            params.leftMargin = x - (colorPickedLocator.width / 2)
            params.topMargin = y - (colorPickedLocator.height / 2)
            colorPickedLocator.layoutParams = params
        }
    }

    private fun setupBrightnessSeekBar() {
        fun setBrightnessSeekBarBackground() {
            val drawable = GradientDrawable().apply {
                colors = intArrayOf(
                    Color.rgb(0, 0, 0),
                    Color.rgb(255, 255, 255)
                )

                orientation = GradientDrawable.Orientation.LEFT_RIGHT
                gradientType = GradientDrawable.LINEAR_GRADIENT
                shape = GradientDrawable.RECTANGLE

                setStroke(2, Color.LTGRAY)
                cornerRadius = 14f
            }

            brightnessSeekBar.background = drawable
        }

        brightnessSeekBar.max = 255
        brightnessSeekBar.progress = 255

        setBrightnessSeekBarBackground()
    }

    private fun setupRedSeekBar() {
        fun setRedSeekBarBackground() {
            val drawable = GradientDrawable().apply {
                colors = intArrayOf(
                    Color.rgb(255, 255, 255),
                    Color.rgb(255, 0, 0)
                )

                orientation = GradientDrawable.Orientation.LEFT_RIGHT
                gradientType = GradientDrawable.LINEAR_GRADIENT
                shape = GradientDrawable.RECTANGLE

                setStroke(2, Color.LTGRAY)
                cornerRadius = 14f
            }

            redSeekBar.background = drawable
        }

        redSeekBar.max = 255
        redSeekBar.progress = 0

        setRedSeekBarBackground()
    }

    private fun setupGreenSeekBar() {
        fun setGreenSeekBarBackground() {
            val drawable = GradientDrawable().apply {
                colors = intArrayOf(
                    Color.rgb(255, 255, 255),
                    Color.rgb(0, 255, 0)
                )

                orientation = GradientDrawable.Orientation.LEFT_RIGHT
                gradientType = GradientDrawable.LINEAR_GRADIENT
                shape = GradientDrawable.RECTANGLE

                setStroke(2, Color.LTGRAY)
                cornerRadius = 14f
            }

            greenSeekBar.background = drawable
        }

        greenSeekBar.max = 255
        greenSeekBar.progress = 0

        setGreenSeekBarBackground()
    }

    private fun setupBlueSeekBar() {
        fun setBlueSeekBarBackground() {
            val drawable = GradientDrawable().apply {
                colors = intArrayOf(
                    Color.rgb(255, 255, 255),
                    Color.rgb(0, 0, 255)
                )

                orientation = GradientDrawable.Orientation.LEFT_RIGHT
                gradientType = GradientDrawable.LINEAR_GRADIENT
                shape = GradientDrawable.RECTANGLE

                setStroke(2, Color.LTGRAY)
                cornerRadius = 14f
            }

            blueSeekBar.background = drawable
        }

        blueSeekBar.max = 255
        blueSeekBar.progress = 0

        setBlueSeekBarBackground()
    }

    private fun setGradientDrawable() {
        val drawable = GradientDrawable().apply {
            colors = intArrayOf(
                colorRGB,
                colorBrightness
            )

            orientation = GradientDrawable.Orientation.TR_BL
            gradientType = GradientDrawable.LINEAR_GRADIENT
            shape = GradientDrawable.RECTANGLE

            setStroke(2, Color.LTGRAY)
            cornerRadius = colorViewCornerRadius
        }

        colorView.background = drawable
    }

    private fun setSelectedColor(color: Int) {
        selectedColor = color
        selectedColorCardView.setCardBackgroundColor(selectedColor)
    }

    fun getSelectedColorHex(): String {
        val colorValue = selectedColor
        val colorUnsigned = colorValue and 0xFFFFFFFF.toInt()
        return String.format("#%08X", colorUnsigned)
    }
}