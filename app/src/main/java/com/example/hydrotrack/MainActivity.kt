package com.example.hydrotrack

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // Variables globales para datos del usuario
    private lateinit var prefs: android.content.SharedPreferences
    private var meta = 0
    private var consumo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

//datos del usuario
        prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE)
        val nombre = prefs.getString("nombre", "Usuario")
        meta = prefs.getInt("metaAgua", 0)
        consumo = prefs.getInt("aguaConsumida", 0)

//conectar elementos del layout
        val tvNombre = findViewById<TextView>(R.id.tvNombre)
        val tvMeta = findViewById<TextView>(R.id.tvMeta)
        val tvConsumo = findViewById<TextView>(R.id.tvConsumo)
        val progreso = findViewById<ProgressBar>(R.id.progresoAgua)

        val btn200 = findViewById<Button>(R.id.btn200)
        val btn300 = findViewById<Button>(R.id.btn300)
        val btn500 = findViewById<Button>(R.id.btn500)
        val btnHistorial = findViewById<Button>(R.id.btnHistorial)

//datos guardados en pantalla
        tvNombre.text = "Hola, $nombre 👋"
        tvMeta.text = "Tu meta: $meta ml"
        actualizarProgreso(tvConsumo, progreso)

//funcion sumar agua
        fun sumarAgua(cantidad: Int) {
            consumo += cantidad
            prefs.edit().putInt("aguaConsumida", consumo).apply()
            actualizarProgreso(tvConsumo, progreso)

            // Si se alcanza o supera la meta
            if (consumo >= meta) {
                Toast.makeText(this, "¡Meta diaria alcanzada! 💧", Toast.LENGTH_LONG).show()
                val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                prefs.edit().putBoolean(fechaHoy, true).apply()
            }
        }

//eventos de suma de agua
        btn200.setOnClickListener { sumarAgua(200) }
        btn300.setOnClickListener { sumarAgua(300) }
        btn500.setOnClickListener { sumarAgua(500) }

        btnHistorial.setOnClickListener {
            startActivity(Intent(this, HistorialActivity::class.java))
        }

//notificacion diaria at 9:00
        val alarmManager = getSystemService(ALARM_SERVICE) as android.app.AlarmManager
        val intent = Intent(this, RecordatorioReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 9)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
        }

        alarmManager.setRepeating(
            android.app.AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            android.app.AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

//fun de actualizar el progreso
    private fun actualizarProgreso(tvConsumo: TextView, progreso: ProgressBar) {
        val porcentaje = if (meta > 0) (consumo * 100 / meta) else 0
        progreso.progress = porcentaje
        tvConsumo.text = "Has tomado: $consumo ml (${porcentaje}%)"
    }

//menu desplegable
    // Infla el menú (lo muestra en los tres puntos)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Define qué hacer cuando el usuario selecciona una opción
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // editar perfil: vuelve al registro sin borrar datos
            //falta configurar
            R.id.action_editar -> {
                startActivity(Intent(this, RegistroActivity::class.java))
                return true
            }

           //ver historial
            R.id.action_historial -> {
                startActivity(Intent(this, HistorialActivity::class.java))
                return true
            }

            //cerrar sesion  y volver a la barra de registro
            R.id.action_salir -> {
                val prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE)
                prefs.edit().clear().apply()
                startActivity(Intent(this, RegistroActivity::class.java))
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
