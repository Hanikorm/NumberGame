package com.hanikorm.game

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.properties.Delegates
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var trueAns = 0
    private var errorCount = 0
    private lateinit var mediaPlayer: MediaPlayer
    private val mess = arrayOf(
        R.raw.yes, R.raw.r1, R.raw.r2, R.raw.r3, R.raw.r4,
        R.raw.r5, R.raw.r6, R.raw.r7, R.raw.r8, R.raw.r9, R.raw.error
    )
    private val images = arrayOf(
        R.drawable.prozt, R.drawable.n1, R.drawable.n2, R.drawable.n3,
        R.drawable.n4, R.drawable.n5, R.drawable.n6, R.drawable.n7,
        R.drawable.n8, R.drawable.n9
    )
    private val operatorImages = arrayOf(R.drawable.plus, R.drawable.minus)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val imageViewSmile = findViewById<ImageView>(R.id.imageViewSmile)
        val textViewP = findViewById<TextView>(R.id.textViewP)
        val buttonOk = findViewById<Button>(R.id.buttonOK)

        generateRandomExpression()

        buttonOk.setOnClickListener {
            val userAnswer = textViewP.text.toString().toIntOrNull()

            if (userAnswer == trueAns) {
                errorCount = 0
                imageViewSmile.setImageResource(R.drawable.notbadsmile)
                playSound(R.raw.yes)
                generateRandomExpression()
            } else {
                errorCount++
                handleIncorrectAnswer()
                buttonOk.isEnabled = false

                val delay = if (errorCount >= 3) 30000L else 5000L

                Handler(Looper.getMainLooper()).postDelayed({
                    nextNumber()
                    buttonOk.isEnabled = true
                }, delay)
            }
        }
    }

    private fun generateRandomExpression() {
        val imageViewNumber1 = findViewById<ImageView>(R.id.imageViewNumber)
        val imageViewOperator = findViewById<ImageView>(R.id.znac)
        val imageViewNumber2 = findViewById<ImageView>(R.id.imageViewNumber2)

        val randomOperator = Random.nextBoolean()
        var num1: Int
        var num2: Int

        do {
            num1 = Random.nextInt(1, 10)
            num2 = Random.nextInt(1, 10)
            trueAns = if (randomOperator) num1 + num2 else num1 - num2
        } while (trueAns < 1 || trueAns > 9)

        // Установка первого числа
        imageViewNumber1.setImageResource(images[num1])

        // Установка оператора
        imageViewOperator.setImageResource(
            if (randomOperator) operatorImages[0] else operatorImages[1]
        )

        // Установка второго числа
        imageViewNumber2.setImageResource(images[num2])

        // Обновление UI после короткой задержки, если необходимо
        /*
        Если вы хотите отображать элементы последовательно с задержкой, можно использовать Handler,
        но это усложнит интерфейс. В данном примере все элементы отображаются одновременно.
        */
    }

    private fun handleIncorrectAnswer() {
        val imageViewSmile = findViewById<ImageView>(R.id.imageViewSmile)
        imageViewSmile.setImageResource(R.drawable.badsmail)

        if (errorCount >= 3) {
            playSound(R.raw.error)
        } else {
            playSound(R.raw.errrr)
        }
    }

    private fun nextNumber() {
        val imageViewSmile = findViewById<ImageView>(R.id.imageViewSmile)
        val textViewP = findViewById<TextView>(R.id.textViewP)

        imageViewSmile.setImageResource(R.drawable.prozt)
        textViewP.text = ""
        generateRandomExpression()
    }

    private fun playSound(soundResId: Int) {
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        mediaPlayer = MediaPlayer.create(this, soundResId)
        mediaPlayer.start()
    }
}

