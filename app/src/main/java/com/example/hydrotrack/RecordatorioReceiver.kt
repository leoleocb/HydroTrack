package com.example.hydrotrack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class RecordatorioReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = NotificationHelper(context)
        notificationHelper.mostrarNotificacion(
            "HydroTrack 💧",
            "Recuerda tomar agua y cumplir tu meta diaria."
        )
        Toast.makeText(context, "Notificación de recordatorio enviada", Toast.LENGTH_SHORT).show()
    }
}
