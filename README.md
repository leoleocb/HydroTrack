# 💧 HydroTrack

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-Android-blueviolet?logo=kotlin&logoColor=white&style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Android%20Studio-IDE-green?logo=androidstudio&logoColor=white&style=for-the-badge"/>
  <img src="https://img.shields.io/badge/XML-UI%20Design-orange?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge"/>
</p>

> **HydroTrack** es una app móvil creada con **Kotlin + XML** que ayuda a las personas a **mejorar sus hábitos de hidratación**.  
> Calcula automáticamente la meta diaria de agua, permite registrar consumos y muestra el progreso diario en un calendario.  
> Diseñada sin SQLite, usando **SharedPreferences** para almacenamiento local.

---

## 🧠 Descripción general

HydroTrack motiva al usuario a cumplir su meta de hidratación diaria mediante una interfaz simple y funcional.  
Toda la información se guarda de forma local, por lo que no requiere conexión a internet ni base de datos.

---

## 🚀 Características principales

- 👤 **Registro de usuario:**  
  Guarda nombre, peso y sexo localmente.  

- 🧮 **Cálculo automático de meta diaria:**
  
Meta diaria (ml) = peso (kg) * 35


- 🚰 **Registro rápido de consumo:**  
Botones de 200, 300 y 500 ml para sumar fácilmente.

- 📊 **Barra de progreso dinámica:**  
Se actualiza en tiempo real y muestra mensaje de “Meta alcanzada” al llegar al 100%.

- 📅 **Calendario de cumplimiento:**  
Marca los días donde se alcanzó la meta diaria.

- 💾 **Persistencia local:**  
Todos los datos se guardan con `SharedPreferences`.

---

## 🧰 Tecnologías utilizadas

| Tecnología | Descripción |
|-------------|-------------|
| 🟣 **Kotlin** | Lógica de negocio y control de UI |
| 🧩 **XML** | Diseño de interfaz (layouts y estilos) |
| 💾 **SharedPreferences** | Almacenamiento de datos locales |
| 🖥️ **Android Studio** | Entorno de desarrollo principal |
| 🧱 **ConstraintLayout** | Diseño adaptable y moderno |

---

## 🖼️ Pantallas principales

1. **Registro de usuario**  
 - Fondo celeste (#E3F2FD)  
 - Campos: nombre, peso, sexo  
 - Botón “Guardar y calcular meta”  

2. **Pantalla principal (consumo diario)**  
 - Muestra meta diaria  
 - Botones para registrar consumo  
 - Barra de progreso y texto de avance  

3. **Calendario de progreso**  
 - Marca visualmente los días cumplidos  

---

## 🗂️ Estructura del proyecto

com.example.hydrotrack/
│
├── MainActivity.kt # Pantalla principal (consumo y progreso)
├── RegistroActivity.kt # Registro de usuario y cálculo de meta
├── CalendarioActivity.kt # Vista de calendario
│
├── utils/
│ └── PrefsHelper.kt # Gestión de SharedPreferences
│
└── res/
├── layout/
│ ├── activity_main.xml
│ ├── activity_registro.xml
│ └── activity_calendario.xml
├── values/
│ ├── colors.xml
│ ├── strings.xml
│ └── styles.xml


---

## 🧪 Ejecución del proyecto

1. Clona este repositorio:
   ```bash
   git clone https://github.com/tuusuario/HydroTrack.git
2.Abre el proyecto en Android Studio

3.Selecciona un emulador o dispositivo físico

4.Ejecuta la app con ▶️ Run App

📚 Conceptos aplicados

Ciclo de vida de actividades (onCreate, onResume)

Comunicación entre pantallas con Intent

Persistencia ligera con SharedPreferences

Uso de ProgressBar y CalendarView

Diseño adaptable con ConstraintLayout

🪄 Mejoras futuras

🔔 Notificaciones diarias de hidratación

📈 Estadísticas semanales

🌙 Modo oscuro

🧍‍♂️ Personalización de objetivos


📍 Proyecto académico – Cibertec
🗓️ Octubre 2025

<p align="center"> <img src="https://img.shields.io/badge/Made%20with-Kotlin-blueviolet?style=for-the-badge&logo=kotlin&logoColor=white"/> <img src="https://img.shields.io/badge/Built%20in-Android%20Studio-green?style=for-the-badge&logo=androidstudio&logoColor=white"/> </p>
📜 Licencia

Este proyecto está bajo la licencia MIT.
Puedes usarlo, modificarlo y compartirlo libremente, dando crédito al autor original.
