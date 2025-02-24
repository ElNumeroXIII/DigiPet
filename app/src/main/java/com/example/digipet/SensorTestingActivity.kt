package com.example.digipet

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.digipet.databinding.ActivityMainBinding
import com.example.digipet.databinding.ActivitySensorTestingBinding

class SensorTestingActivity : AppCompatActivity(), SensorEventListener {

    // ViewBinding para activity_main.xml (asegúrate que el nombre del archivo sea activity_main.xml)
    private lateinit var binding: ActivitySensorTestingBinding

    // SensorManager y sensores
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null
    private var lightSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar el layout usando viewBinding
        binding = ActivitySensorTestingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar SensorManager y obtener sensores
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        // Botón para salir: cuando se pulse, se finaliza la actividad
        binding.btSalir.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        // Registrar los sensores con una tasa de actualización NORMAL
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        gyroscope?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        lightSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        // Anular el registro de todos los sensores para ahorrar batería
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    // Actualizar los TextView del acelerómetro
                    binding.tvAcelerometroEjex.text = "X: ${"%.2f".format(it.values[0])}"
                    binding.tvAcelerometroEjey.text = "Y: ${"%.2f".format(it.values[1])}"
                    binding.tvAcelerometroEjez.text = "Z: ${"%.2f".format(it.values[2])}"
                }
                Sensor.TYPE_GYROSCOPE -> {
                    // Actualizar los TextView del giroscopio
                    binding.tvGiroscopioEjex.text = "X: ${"%.2f".format(it.values[0])}"
                    binding.tvGiroscopioEjey.text = "Y: ${"%.2f".format(it.values[1])}"
                    binding.tvGiroscopioEjez.text = "Z: ${"%.2f".format(it.values[2])}"
                }
                Sensor.TYPE_LIGHT -> {
                    // Actualizar el TextView del sensor de luz
                    binding.tvSensorluz.text = "Sensor: ${"%.2f".format(it.values[0])}"
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Puedes implementar cambios de precisión si lo deseas
    }
}
