package com.example.hydrotrack

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var tvSaludo: TextView
    private lateinit var tvMeta: TextView
    private lateinit var tvProgreso: TextView
    private lateinit var progressBar: ProgressBar

    private var cantidadTomada = 0
    private var metaDiaria = 0
    private var nombre: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drawer)

        // --- Drawer y Toolbar ---
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //datos usuario
        val prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE)
        nombre = prefs.getString("nombre", "Usuario")
        metaDiaria = prefs.getInt("metaAgua", 0)

        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val ultimaFecha = prefs.getString("ultimaFecha", "")

        // Reiniciar progreso si es otro día
        if (ultimaFecha != fechaHoy) {
            cantidadTomada = 0
            prefs.edit()
                .putString("ultimaFecha", fechaHoy)
                .putInt("cantidadTomada", 0)
                .apply()
        } else {
            cantidadTomada = prefs.getInt("cantidadTomada", 0)
        }

        // --- Header del Drawer ---
        val header = navView.getHeaderView(0)
        val tvHeader = header.findViewById<TextView>(R.id.tvHeaderNombre)
        tvHeader.text = nombre

        // --- Vistas principales ---
        tvSaludo = findViewById(R.id.tvSaludo)
        tvMeta = findViewById(R.id.tvMeta)
        tvProgreso = findViewById(R.id.tvProgreso)
        progressBar = findViewById(R.id.progressBar)

        val btn200 = findViewById<Button>(R.id.btn200)
        val btn300 = findViewById<Button>(R.id.btn300)
        val btn500 = findViewById<Button>(R.id.btn500)
        val btnOtra = findViewById<Button>(R.id.btnOtra)
        val btnHistorial = findViewById<Button>(R.id.btnHistorial)

        val btnRestar200 = findViewById<Button>(R.id.btnRestar200)
        val btnRestar300 = findViewById<Button>(R.id.btnRestar300)
        val btnRestar500 = findViewById<Button>(R.id.btnRestar500)
        val btnRestarOtra = findViewById<Button>(R.id.btnRestarOtra)

        // --- Mostrar saludo y meta ---
        tvSaludo.text = "Hola, $nombre 👋"
        tvMeta.text = "Tu meta: ${metaDiaria} ml"
        actualizarProgreso()

        // --- Botones SUMAR ---
        btn200.setOnClickListener { agregarAgua(200) }
        btn300.setOnClickListener { agregarAgua(300) }
        btn500.setOnClickListener { agregarAgua(500) }
        btnOtra.setOnClickListener { abrirDialogoPersonalizado(true) }

        // --- Botones RESTAR ---
        btnRestar200.setOnClickListener { restarAgua(200) }
        btnRestar300.setOnClickListener { restarAgua(300) }
        btnRestar500.setOnClickListener { restarAgua(500) }
        btnRestarOtra.setOnClickListener { abrirDialogoPersonalizado(false) }

        // --- Botón Historial ---
        btnHistorial.setOnClickListener {
            startActivity(Intent(this, HistorialActivity::class.java))
        }

        // --- Menú lateral ---
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_editar -> {
                    startActivity(Intent(this, EditarPerfilActivity::class.java))
                    true
                }
                R.id.nav_historial -> {
                    startActivity(Intent(this, HistorialActivity::class.java))
                    true
                }
                R.id.nav_estadisticas -> {
                    startActivity(Intent(this, EstadisticasActivity::class.java))
                    true
                }
                R.id.nav_salir -> {
                    prefs.edit().clear().apply()
                    startActivity(Intent(this, RegistroActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    // --- FUNCIONES LÓGICAS ---

    private fun agregarAgua(cantidad: Int) {
        cantidadTomada += cantidad
        guardarProgreso()
        actualizarProgreso()
        Toast.makeText(this, "+${cantidad}ml agregados ", Toast.LENGTH_SHORT).show()
    }

    private fun restarAgua(cantidad: Int) {
        cantidadTomada -= cantidad
        if (cantidadTomada < 0) cantidadTomada = 0
        guardarProgreso()
        actualizarProgreso()
        Toast.makeText(this, "-${cantidad}ml restados ", Toast.LENGTH_SHORT).show()
    }

    private fun abrirDialogoPersonalizado(esSuma: Boolean) {
        val input = EditText(this)
        input.hint = "Cantidad en ml"
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this)
            .setTitle(if (esSuma) "Agregar cantidad personalizada" else "Restar cantidad personalizada")
            .setView(input)
            .setPositiveButton("Aceptar") { _, _ ->
                val cantidad = input.text.toString().toIntOrNull()
                if (cantidad != null && cantidad > 0) {
                    if (esSuma) agregarAgua(cantidad) else restarAgua(cantidad)
                } else {
                    Toast.makeText(this, "Ingresa un número válido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun actualizarProgreso() {
        if (metaDiaria <= 0) {
            tvProgreso.text = "Has tomado: 0 ml (0%)"
            progressBar.progress = 0
            return
        }

        val porcentaje = (cantidadTomada * 100) / metaDiaria
        progressBar.progress = porcentaje.coerceAtMost(100)
        tvProgreso.text = "Has tomado: ${cantidadTomada} ml (${porcentaje}%)"

        val prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE)
        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (cantidadTomada >= metaDiaria) {
            if (!prefs.getBoolean(fechaHoy, false)) {
                prefs.edit().putBoolean(fechaHoy, true).apply()
                Toast.makeText(this, "🎉 ¡Meta diaria alcanzada! 🎉", Toast.LENGTH_LONG).show()
            }
        } else if (prefs.getBoolean(fechaHoy, false)) {
            prefs.edit().putBoolean(fechaHoy, false).apply()
            Toast.makeText(this, "Meta diaria desmarcada ⚠️", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarProgreso() {
        val prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE)
        prefs.edit().putInt("cantidadTomada", cantidadTomada).apply()
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE)

        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val ultimaFecha = prefs.getString("ultimaFecha", "")

        // Refrescar nombre actualizado
        nombre = prefs.getString("nombre", "Usuario")
        metaDiaria = prefs.getInt("metaAgua", 0)

        // Actualizar saludo y header del Drawer
        tvSaludo.text = "Hola, $nombre "
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val header = navView.getHeaderView(0)
        val tvHeader = header.findViewById<TextView>(R.id.tvHeaderNombre)
        tvHeader.text = nombre

        // Reiniciar progreso si cambió el día
        if (ultimaFecha != fechaHoy) {
            cantidadTomada = 0
            prefs.edit()
                .putString("ultimaFecha", fechaHoy)
                .putInt("cantidadTomada", 0)
                .apply()
            progressBar.progress = 0
            tvProgreso.text = "Has tomado: 0 ml (0%)"
            Toast.makeText(this, " Nuevo día, tu progreso se ha reiniciado", Toast.LENGTH_SHORT).show()
        } else {
            cantidadTomada = prefs.getInt("cantidadTomada", 0)
        }

        tvMeta.text = "Tu meta: ${metaDiaria} ml"
        actualizarProgreso()
    }
}
