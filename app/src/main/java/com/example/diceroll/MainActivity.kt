package com.example.diceroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.animation.AccelerateDecelerateInterpolator


// (If you added the shake animation imports, keep them)

class MainActivity : AppCompatActivity() {

    private var rollCount = 0
    private lateinit var rollCountText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Restore count after rotation
        rollCount = savedInstanceState?.getInt("ROLL_COUNT") ?: 0

        val rollButtonMA: Button = findViewById(R.id.button_AM)
        val resetCountBtn: Button = findViewById(R.id.resetCountBtn)
        rollCountText = findViewById(R.id.rollCountText)
        updateRollCountUI()

        // Remove this if you don't want an auto-roll at launch
        // rollDiceMA()

        rollButtonMA.setOnClickListener {
            rollDiceMA()
        }

        resetCountBtn.setOnClickListener {
            rollCount = 0
            updateRollCountUI()
            // Optional: clear dice images
            findViewById<ImageView>(R.id.imageView).setImageResource(0)
            findViewById<ImageView>(R.id.imageView2).setImageResource(0)
        }
    }

    private fun shakeThenSet(view: ImageView, drawableId: Int, durationMs: Long = 450) {
        val shake = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0f, -18f, 18f, -12f, 12f, -6f, 6f, 0f),
                ObjectAnimator.ofFloat(view, "rotation", 0f, -10f, 10f, -6f, 6f, -3f, 3f, 0f),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.06f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.06f, 1f)
            )
            duration = durationMs
            interpolator = AccelerateDecelerateInterpolator()
        }

        shake.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.setImageResource(drawableId)
            }
        })

        shake.start()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("ROLL_COUNT", rollCount)
        super.onSaveInstanceState(outState)
    }

    private fun updateRollCountUI() {
        rollCountText.text = getString(R.string.rolls_label, rollCount)

    }

    private fun rollDiceMA() {
        // Increment count each time the user rolls
        rollCount++
        updateRollCountUI()

        val diceMA = DiceMA(6)
        val dice2 = DiceMA(6)

        val cubeRoll  = diceMA.rollMA()
        val cubeRoll2 = dice2.rollMA()

        val diceImage:  ImageView = findViewById(R.id.imageView)
        val diceImage2: ImageView = findViewById(R.id.imageView2)

        // If you’re using shake animation, call shakeThenSet(...)
        // Otherwise set directly:
        shakeThenSet(diceImage, getDiceDrawable(cubeRoll))
        shakeThenSet(diceImage2, getDiceDrawable(cubeRoll2))

    }

    private fun getDiceDrawable(value: Int): Int = when (value) {
        1 -> R.drawable.dice1
        2 -> R.drawable.dice2
        3 -> R.drawable.dice3
        4 -> R.drawable.dice4
        5 -> R.drawable.dice5
        else -> R.drawable.dice6
    }
}

class DiceMA(private val numSidesMA: Int) {
    fun rollMA(): Int = (1..numSidesMA).random()
}
