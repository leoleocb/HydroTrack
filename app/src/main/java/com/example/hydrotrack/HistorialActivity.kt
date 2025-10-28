package com.example.hydrotrack

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class HistorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val tvCumplidos = findViewById<TextView>(R.id.tvCumplidos)
        val prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE)
        val allKeys = prefs.all

        val mesActual = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
        val diasCumplidos = allKeys.filter { (key, value) ->
            key.startsWith(mesActual) && value == true
        }.size

        tvCumplidos.text = "Has cumplido $diasCumplidos días este mes."

        calendarView.setOnDateChangeListener { _, year, month, day ->
            val fecha = String.format("%04d-%02d-%02d", year, month + 1, day)
            val cumplido = prefs.getBoolean(fecha, false)
            val mensaje = if (cumplido) "Meta cumplida ese día 💧" else "No cumpliste la meta"
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        }
    }
}
