package com.example.hydrotrack

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import java.text.SimpleDateFormat
import java.util.*

class HistorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        // --- Toolbar con botón de regreso ---
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarHistorial)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // --- Referencias de vistas ---
        val calendarView = findViewById<CalendarView>(R.id.customCalendarView)
        val tvCumplidos = findViewById<TextView>(R.id.tvCumplidos)
        val prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE)

        // --- Formatos y variables ---
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
        val allKeys = prefs.all
        val eventos = mutableListOf<EventDay>()

        // --- Filtrar los días cumplidos de este mes ---
        val diasCumplidos = allKeys.filter { (key, value) ->
            key.startsWith(currentMonth) && value == true
        }

        // --- Crear eventos visuales (círculos azules) ---
        for ((key, _) in diasCumplidos) {
            try {
                val date = sdf.parse(key)
                val calendar = Calendar.getInstance()
                calendar.time = date!!

                val shape = GradientDrawable()
                shape.shape = GradientDrawable.OVAL
                shape.setColor(0xFF2196F3.toInt()) // Azul brillante
                shape.setSize(18, 18)

                eventos.add(EventDay(calendar, shape))
            } catch (_: Exception) {}
        }

        // --- Mostrar los eventos en el calendario ---
        calendarView.setEvents(eventos)

        // --- Mostrar cantidad de días cumplidos ---
        tvCumplidos.text = "Has cumplido ${diasCumplidos.size} días este mes 💧"

        // --- Acción al tocar un día ---
        calendarView.setOnDayClickListener(object : com.applandeo.materialcalendarview.listeners.OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val fechaSeleccionada = sdf.format(eventDay.calendar.time)
                val cumplido = prefs.getBoolean(fechaSeleccionada, false)
                val mensaje = if (cumplido)
                    "🎉 El ${fechaSeleccionada} cumpliste tu meta 💧"
                else
                    "😔 El ${fechaSeleccionada} no cumpliste tu meta"
                Toast.makeText(this@HistorialActivity, mensaje, Toast.LENGTH_SHORT).show()
            }
        })

    }
}
