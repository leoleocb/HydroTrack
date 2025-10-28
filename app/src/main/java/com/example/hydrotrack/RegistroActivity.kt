package com.example.hydrotrack

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_registro)

        val prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etPeso = findViewById<EditText>(R.id.etPeso)
        val rbHombre = findViewById<RadioButton>(R.id.rbHombre)
        val rbMujer = findViewById<RadioButton>(R.id.rbMujer)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        // Si ya hay datos guardados, ir directo al MainActivity
        if (prefs.contains("nombre") && prefs.contains("metaAgua")) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val pesoTexto = etPeso.text.toString().trim()
            val sexo = when {
                rbHombre.isChecked -> "Hombre"
                rbMujer.isChecked -> "Mujer"
                else -> ""
            }

            // Validaciones
            if (nombre.isEmpty() || pesoTexto.isEmpty() || sexo.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val peso = pesoTexto.toIntOrNull()
            if (peso == null || peso <= 0) {
                Toast.makeText(this, "Ingresa un peso válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val meta = peso * 35 // Cálculo meta diaria

            prefs.edit()
                .putString("nombre", nombre)
                .putInt("peso", peso)
                .putString("sexo", sexo)
                .putInt("metaAgua", meta)
                .apply()

            Toast.makeText(this, "Datos guardados. Tu meta diaria es $meta ml 💧", Toast.LENGTH_LONG).show()

            // Ir al MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
