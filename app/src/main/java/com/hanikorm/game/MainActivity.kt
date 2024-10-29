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
    private var errorCount = 0 // Счетчик ошибок
    private lateinit var mediaPlayer: MediaPlayer
    private val mess = arrayOf(R.raw.yes, R.raw.r1, R.raw.r2, R.raw.r3, R.raw.r4, R.raw.r5, R.raw.r6, R.raw.r7, R.raw.r8, R.raw.r9, R.raw.error)
    private val images = arrayOf(R.drawable.prozt, R.drawable.n1, R.drawable.n2, R.drawable.n3, R.drawable.n4, R.drawable.n5, R.drawable.n6, R.drawable.n7, R.drawable.n8, R.drawable.n9)
    private val operatorImages = arrayOf(R.drawable.plus, R.drawable.minus) // Изображения знаков + и -

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val imageViewNumber = findViewById<ImageView>(R.id.imageViewNumber)
        val imageViewSmile = findViewById<ImageView>(R.id.imageViewSmile)
        val textViewP = findViewById<TextView>(R.id.textViewP)
        val buttonOk = findViewById<Button>(R.id.buttonOK)

        generateRandomExpression() // Генерация случайного выражения

        buttonOk.setOnClickListener {
            val userAnswer = textViewP.text.toString().toIntOrNull()

            // Проверка верного ответа
            if (userAnswer == trueAns) {
                errorCount = 0  // Сброс счётчика ошибок
                imageViewSmile.setImageResource(R.drawable.notbadsmile)
                playSound(R.raw.yes)
                generateRandomExpression() // Генерация нового выражения
            } else {
                errorCount++
                handleIncorrectAnswer()
                buttonOk.isEnabled = false // Блокируем кнопку на 3 секунды

                val delay = if (errorCount >= 3) 30000L else 3000L // Задержка 15 секунд после 3 ошибок, иначе 3 секунды

                Handler(Looper.getMainLooper()).postDelayed({
                    nextNumber()
                    buttonOk.isEnabled = true
                }, delay)
            }
        }
    }

    private fun generateRandomExpression() {
        val imageViewNumber = findViewById<ImageView>(R.id.imageViewNumber)
        val randomOperator = Random.nextBoolean()
        var num1: Int
        var num2: Int

        do {
            num1 = Random.nextInt(1, 10)
            num2 = Random.nextInt(1, 10)
            trueAns = if (randomOperator) num1 + num2 else num1 - num2
        } while (trueAns < 1 || trueAns > 9)


        imageViewNumber.setImageResource(images[num1])


        Handler(Looper.getMainLooper()).postDelayed({
            imageViewNumber.setImageResource(if (randomOperator) operatorImages[0] else operatorImages[1])

            Handler(Looper.getMainLooper()).postDelayed({
                imageViewNumber.setImageResource(images[num2])
            }, 5000)
        }, 5000)
    }

    private fun handleIncorrectAnswer() {
        val imageViewSmile = findViewById<ImageView>(R.id.imageViewSmile)
        imageViewSmile.setImageResource(R.drawable.badsmail)

        if (errorCount >= 3) {
            // После 3 ошибок проигрываем звук ошибки
            playSound(R.raw.error)
        } else {
            // Иначе проигрываем звук, соответствующий числу на изображении
            playSound(mess[trueAns])
        }
    }

    private fun nextNumber() {
        val imageViewSmile = findViewById<ImageView>(R.id.imageViewSmile)
        val textViewP = findViewById<TextView>(R.id.textViewP)

        imageViewSmile.setImageResource(R.drawable.prozt)
        textViewP.text = ""
        generateRandomExpression() // Генерация нового выражения
    }

    private fun playSound(soundResId: Int) {
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        mediaPlayer = MediaPlayer.create(this, soundResId)
        mediaPlayer.start()
    }
}

