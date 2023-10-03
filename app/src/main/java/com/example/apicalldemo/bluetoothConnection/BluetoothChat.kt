package com.example.apicalldemo.bluetoothConnection

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.apicalldemo.base.MainActivity
import com.example.apicalldemo.databinding.ActivityBlueToothchatBinding
import com.example.apicalldemo.utils.BLUETOOTH_DISABLE_ACTION


open class BluetoothChat : AppCompatActivity() {
    private lateinit var binding: ActivityBlueToothchatBinding
    private var requestBluetooth: ActivityResultLauncher<Intent>? = null
    private var pairedDevices: Set<BluetoothDevice>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlueToothchatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bluetoothManager =
            applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter:BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()


        val requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach { data ->
                    if (bluetoothManager.adapter != null) {

                        if (!bluetoothManager.adapter!!.isEnabled) {
                            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                            requestBluetooth?.launch(intent)
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Device does not support",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    Log.d(javaClass.simpleName, "permission ${data.key} = ${data.value}")
                }
            }
        binding.title.setOnClickListener {
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(mReceiver, filter)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                bluetoothAdapter.startDiscovery()
            }


        }



        requestBluetooth =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    Toast.makeText(this, "Bluetooth is enabled", Toast.LENGTH_SHORT).show()
                        /* pairedDevices!!.forEach { list.add(it) }*/

                    Log.e(javaClass.simpleName, "onCreate: granted")

                } else {
                    Toast.makeText(this, "Permission canceled", Toast.LENGTH_LONG).show()
                    Log.e(javaClass.simpleName, "onCreate: deny")
                    finish()
                }
            }

        val requestBluetoothOff =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    Toast.makeText(this, "Bluetooth is Off", Toast.LENGTH_SHORT).show()
                    Log.e(javaClass.simpleName, "onCreate: Off")

                } else {
                    Toast.makeText(this, "On", Toast.LENGTH_LONG).show()
                    Log.e(javaClass.simpleName, "onCreate: On")
                }
            }



        binding.onOff.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if ((ContextCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.BLUETOOTH
                            )
                                    == PackageManager.PERMISSION_GRANTED
                                    ) && (ContextCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.BLUETOOTH_CONNECT
                            )
                                    == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.BLUETOOTH_SCAN
                            )
                                    == PackageManager.PERMISSION_GRANTED) /*&& (ContextCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                                    == PackageManager.PERMISSION_GRANTED)*/
                        ) {
                            bluetoothManager.adapter.startDiscovery()
                            if (bluetoothManager.adapter != null) {
                                if (!bluetoothManager.adapter!!.isEnabled) {
                                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                                    requestBluetooth?.launch(intent)

                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Bluetooth already on",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Device does not support",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            requestMultiplePermissions.launch(
                                arrayOf(
                                    Manifest.permission.BLUETOOTH,
                                    Manifest.permission.BLUETOOTH_CONNECT,
                                    Manifest.permission.BLUETOOTH_SCAN,
                                )
                            )
                        }

                    } else {
                        if (bluetoothManager.adapter != null) {
                            if (!bluetoothManager.adapter!!.isEnabled) {
                                //bluetoothManager.adapter.disable()
                                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                                requestBluetooth?.launch(intent)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Bluetooth already on",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Device does not support",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }

                false -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if ((ContextCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.BLUETOOTH_CONNECT
                            )
                                    == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.BLUETOOTH
                            )
                                    == PackageManager.PERMISSION_GRANTED
                                    )
                        ) {
                            if (bluetoothManager.adapter != null) {
                                Log.e(
                                    javaClass.simpleName,
                                    "isEnabled 13: " + bluetoothManager.adapter!!.isEnabled
                                )
                                if (bluetoothManager.adapter!!.isEnabled) {
                                    val intent = Intent(BLUETOOTH_DISABLE_ACTION)
                                    requestBluetoothOff.launch(intent)
                                }

                            }
                        }
                    } else {
                        if (bluetoothManager.adapter != null) {
                            Log.e(
                                javaClass.simpleName,
                                "isEnabled: " + bluetoothManager.adapter!!.isEnabled
                            )
                            if (bluetoothManager.adapter!!.isEnabled) {
                                val intent = Intent(BLUETOOTH_DISABLE_ACTION)
                                requestBluetoothOff.launch(intent)
                            }

                        }

                    }
                }
            }
        }
    }
     private fun getListOfPairedDevices() {

    }

     private fun getConnectedDevices(): List<BluetoothDevice?>? {
        val btManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
         if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
             Log.e(javaClass.simpleName, "getConnectedDevices: "+btManager.getConnectedDevices(BluetoothProfile.GATT) )

        }
        return btManager.getConnectedDevices(BluetoothProfile.GATT)
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action

            //Device found
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Get the BluetoothDevice object from the Intent
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                // Add the name and address to an array adapter to show in a list

                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (device != null) {
                        Log.e(javaClass.simpleName, "onReceive: "+device.name )
                    }
                    return
                }

            }
        }
    }

}