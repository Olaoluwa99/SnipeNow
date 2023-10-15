package com.easit.floatingbuttonsnipingtool

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.easit.floatingbuttonsnipingtool.ui.theme.FloatingButtonSnipingToolTheme
import java.io.File

class MainActivity : ComponentActivity() {

    private val CODE_DRAW_OVER_OTHER_APP_PERMISSION = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FloatingButtonSnipingToolTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(cxt = this@MainActivity, int = intent)
                }
            }
        }
    }

    @Composable
    fun MainScreen(cxt: Context, int: Intent) {
        //Check for image scanner value
        var scannedImageURI = ""
        val context = LocalContext.current
        val show = remember { mutableStateOf<Boolean>(false) }
        val showBitmap = remember { mutableStateOf<Bitmap?>(null) }

        try {
            scannedImageURI = int.getStringExtra("ScannedImageURI").toString()
            if (scannedImageURI.isNotBlank() && scannedImageURI != "" && scannedImageURI != "null"){
                val path =
                    "${context.filesDir.path}/SCAN.$scannedImageURI.jpg"
                val fileURI = Uri.fromFile(File(path))
                showBitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(fileURI.toString()))
                show.value = true
            }
        }catch (e: Exception){
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
        }

        //
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(state = ScrollState(0)),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //
            Text(
                text = "Floating Sniping Tool",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Instruction",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )

            //
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Click 'launch' to start")
                Text(text = "2. Accept all permissions.")
                Text(text = "2. On prompt, drag over area to screenshot.")
            }

            //
            Button(
                onClick = {
                    launchScanner()
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //
                Text(text = "Launch")
            }

            //RESULT
            Text(
                text = "Result",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )

            if (show.value){
                showBitmap.value?.let { Image(bitmap = it.asImageBitmap(), contentDescription = "") }
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

@Preview(showBackground = true)
@Composable
fun FloatingSnipingToolPreview() {
    FloatingButtonSnipingToolTheme() {
        //MainScreen()
    }
}