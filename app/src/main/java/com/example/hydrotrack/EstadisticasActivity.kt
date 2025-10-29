package com.example.hydrotrack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.applandeo.materialcalendarview.CalendarView
import java.text.SimpleDateFormat
import java.util.*

class EstadisticasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadisticas)

        // --- Toolbar con botón atrás ---
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarEstadisticas)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // --- Referencias de vistas ---
        val tvMetaActual = findViewById<TextView>(R.id.tvMetaActual)
        val tvCantidadHoy = findViewById<TextView>(R.id.tvCantidadHoy)
        val tvPorcentaje = findViewById<TextView>(R.id.tvPorcentaje)
        val tvDiasCumplidos = findViewById<TextView>(R.id.tvDiasCumplidos)
        val tvMensaje = findViewById<TextView>(R.id.tvMensaje)

        // --- Acceso a datos guardados ---
        val prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE)
        val meta = prefs.getInt("metaAgua", 0)
        val cantidadTomada = prefs.getInt("cantidadTomada", 0)
        val porcentaje = if (meta > 0) (cantidadTomada * 100) / meta else 0

        // --- Contar días cumplidos del mes actual ---
        val allKeys = prefs.all
        val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
        val diasCumplidos = allKeys.filter { (key, value) ->
            key.startsWith(currentMonth) && value == true
        }.size

        // --- Mostrar datos ---
        tvMetaActual.text = "Meta diaria: ${meta} ml"
        tvCantidadHoy.text = "Hoy has tomado: ${cantidadTomada} ml"
        tvPorcentaje.text = "Progreso: ${porcentaje}%"
        tvDiasCumplidos.text = "Días cumplidos este mes: ${diasCumplidos}"

        // --- Mensaje motivacional dinámico ---
        tvMensaje.text = when {
            porcentaje >= 100 -> "🎉 ¡Excelente! Cumpliste tu meta de hoy 💪"
            porcentaje >= 50 -> "¡Vas muy bien! Ya llevas más de la mitad 💧"
            else -> "Vamos, no te rindas. Aún puedes lograrlo 💦"
        }
    }
}
