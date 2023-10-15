package com.easit.floatingbuttonsnipingtool

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getSystemService
import com.easit.floatingbuttonsnipingtool.ui.theme.FloatingButtonSnipingToolTheme

class TransActivity : ComponentActivity() {

    private val CODE_DRAW_OVER_OTHER_APP_PERMISSION = 200
    private val REQUEST_CODE = 1001
    var DeX = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FloatingButtonSnipingToolTheme{
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {

                    val config = resources.configuration
                    try {
                        val configClass: Class<*> = config.javaClass
                        if (configClass.getField("SEM_DESKTOP_MODE_ENABLED").getInt(configClass) ==
                            configClass.getField("semDesktopModeEnabled").getInt(config)
                        ) {
                            DeX = true
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    startProjection()

                    Box (
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent)
                            .clickable{ finish() }
                    ){

                    }
                }
            }
        }
    }

    //
    private fun startProjection() {
        val mProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE)
    }

    private fun startProjectionNew() {
        val mediaProjectionManager = getSystemService(MediaProjectionManager::class.java)
        var mediaProjection : MediaProjection

        val startMediaProjection = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                mediaProjection = mediaProjectionManager.getMediaProjection(
                    result.resultCode, result.data!!
                )
                startService(FloatingWindow.getStartIntent(this, result.resultCode, result.data!!))
            }
        }
        startMediaProjection.launch(mediaProjectionManager.createScreenCaptureIntent())
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //val i = FloatingWindow.getStartIntent(this, resultCode, data)
                startService(FloatingWindow.getStartIntent(this, resultCode, data))
                finish()
            }
        }
    }



}