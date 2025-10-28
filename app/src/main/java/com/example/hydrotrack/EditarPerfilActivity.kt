package com.example.hydrotrack

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditarPerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        val prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE)
        val nombre = prefs.getString("nombre", "")
        val peso = prefs.getInt("peso", 0)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etPeso = findViewById<EditText>(R.id.etPeso)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        etNombre.setText(nombre)
        etPeso.setText(if (peso > 0) peso.toString() else "")

        btnGuardar.setOnClickListener {
            val nuevoNombre = etNombre.text.toString()
            val nuevoPeso = etPeso.text.toString().toIntOrNull() ?: 0
            val meta = nuevoPeso * 35

            prefs.edit()
                .putString("nombre", nuevoNombre)
                .putInt("peso", nuevoPeso)
                .putInt("metaAgua", meta)
                .apply()

            Toast.makeText(this, "Datos actualizados ✅", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
