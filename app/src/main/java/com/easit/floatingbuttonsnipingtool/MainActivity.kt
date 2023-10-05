package com.easit.floatingbuttonsnipingtool

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.easit.floatingbuttonsnipingtool.ui.theme.FloatingButtonSnipingToolTheme

class MainActivity : ComponentActivity() {

    private val CODE_DRAW_OVER_OTHER_APP_PERMISSION = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FloatingButtonSnipingToolTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        //
                        Button(onClick = {
                            launchScanner()
                        }) {
                            //
                        }
                    }
                }
            }
        }
    }

    fun launchScanner(){

        //finish()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)

            Toast.makeText(this, "Please Grant STORAGE Permission", Toast.LENGTH_SHORT).show()
            requestStoragePermission()
        }

        checkOverlayPermission()
        // start projection
        val i = Intent(this, TransActivity::class.java)
        startActivity(i)

        /*
        // stop projection
        val stopButton: Button = findViewById<Button>(R.id.stopButton)
        stopButton.setOnClickListener {
            stopServiceIfRunning()
        }*/
    }

    //Permission STORAGE
    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PackageManager.PERMISSION_GRANTED
        )
    }

    //Permission OVERLAY
    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Please Grant Overlay Permission 3", Toast.LENGTH_SHORT).show()

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(
                intent,
                CODE_DRAW_OVER_OTHER_APP_PERMISSION
            )
        } else {
            Toast.makeText(this, "Service should start 1", Toast.LENGTH_SHORT).show()
        }
    }
}