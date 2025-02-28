package com.example.pomodoro
import android.content.Context
import android.os.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private lateinit var textViewTimer: TextView
    private lateinit var buttonStart: Button
    private lateinit var editTextTime: EditText
    private var timer: Timer? = null
    private var timeLeft = 25 * 60  

    private var pomodoroCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewTimer = findViewById(R.id.textViewTimer)
        buttonStart = findViewById(R.id.buttonStart)
        editTextTime = findViewById(R.id.editTextNumero)

        buttonStart.setOnClickListener {
            val inputTime = editTextTime.text.toString().toIntOrNull()
            if (inputTime != null && inputTime > 0) {
                timeLeft = inputTime * 60
            }
            startPomodoro()
        }
    }

    private fun startPomodoro() {
        buttonStart.isEnabled = false  

        timer = timer(period = 1000) {
            runOnUiThread {
                if (timeLeft > 0) {
                    timeLeft--
                    updateTimerText()
                } else {
                    timer?.cancel()
                    pomodoroCount++
                    triggerVibration(500)
                    if (pomodoroCount % 4 == 0) {
                        startBreak(15 * 60) // Pausa longa
                    } else {
                        startBreak(5 * 60) // Pausa curta
                    }
                }
            }
        }
    }

    private fun startBreak(duration: Int) {
        timeLeft = duration
        buttonStart.isEnabled = true // Permite iniciar novo ciclo
    }

    private fun updateTimerText() {
        val minutes = timeLeft / 60
        val seconds = timeLeft % 60
        textViewTimer.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun triggerVibration(duration: Long) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(duration)
        }
    }
}
