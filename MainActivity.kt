<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.VIBRATE" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MyApplication"
        tools:targetApi="31">

        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>

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

<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <EditText
        android:id="@+id/editTextNumero"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:inputType="number"
        android:hint="Digite o tempo (minutos)"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintMarginTop="32dp" />

    <TextView
        android:id="@+id/textViewTimer"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="25:00"
        android:textSize="96sp"
        android:textStyle="bold"
        app:layout_constraintTop_toBottomOf="@id/editTextNumero"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintMarginTop="24dp" />

    <Button
        android:id="@+id/buttonStart"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="12dp"
        android:text="ComeÃ§ar"
        app:layout_constraintTop_toBottomOf="@id/textViewTimer"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintMarginTop="24dp" />

</androidx.constraintlayout.widget.ConstraintLayout>
