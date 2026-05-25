package com.example.kellera.biosystem

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.kellera.R
import java.io.InputStream
import java.util.UUID

class BiometricActivity : Activity() {

    private var bluetoothAdapter: BluetoothAdapter? = null

    private lateinit var txtBluetooth: TextView
    private lateinit var txtStatus: TextView
    private lateinit var txtDevices: TextView

    private var socket: BluetoothSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_biometric)

        txtBluetooth = findViewById(R.id.txtBluetooth)
        txtStatus = findViewById(R.id.txtStatus)
        txtDevices = findViewById(R.id.txtDevices)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {

            txtBluetooth.text = "Bluetooth: NÃO SUPORTADO"

            return
        }

        if (bluetoothAdapter!!.isEnabled) {

            txtBluetooth.text = "Bluetooth: LIGADO"

            procurarESP32()

        } else {

            txtBluetooth.text = "Bluetooth: DESLIGADO"
        }
    }

    @SuppressLint("MissingPermission")
    private fun procurarESP32() {

        val pairedDevices: Set<BluetoothDevice>? =
            bluetoothAdapter?.bondedDevices

        if (pairedDevices.isNullOrEmpty()) {

            txtStatus.text = "Status: nenhum dispositivo"

            return
        }

        for (device in pairedDevices) {

            if (device.name.contains("ESP32")) {

                txtDevices.text = "Conectado: ${device.name}"

                conectarESP32(device)

                return
            }
        }

        txtStatus.text = "ESP32 não encontrado"
    }

    @SuppressLint("MissingPermission")
    private fun conectarESP32(device: BluetoothDevice) {

        txtStatus.text = "Conectando ao ESP32..."

        Thread {

            try {

                val uuid: UUID =
                    UUID.fromString(
                        "00001101-0000-1000-8000-00805F9B34FB"
                    )

                socket =
                    device.createRfcommSocketToServiceRecord(uuid)

                socket?.connect()

                runOnUiThread {

                    txtStatus.text =
                        "ESP32 CONECTADO"
                }

                receberDados()

            } catch (e: Exception) {

                runOnUiThread {

                    txtStatus.text =
                        "Falha na conexão"

                    Toast.makeText(
                        this,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }.start()
    }

    private fun receberDados() {

        Thread {

            try {

                val inputStream: InputStream =
                    socket!!.inputStream

                val buffer = ByteArray(1024)

                while (true) {

                    val bytes =
                        inputStream.read(buffer)

                    val mensagem =
                        String(buffer, 0, bytes).trim()

                    runOnUiThread {

                        if (mensagem.contains("TEMP:")) {

                            val temperatura =
                                mensagem.replace("TEMP:", "")

                            txtStatus.text =
                                "🌡️ $temperatura °C"
                        }
                    }
                }

            } catch (e: Exception) {

                runOnUiThread {

                    txtStatus.text =
                        "Erro recebendo dados"
                }
            }

        }.start()
    }
}