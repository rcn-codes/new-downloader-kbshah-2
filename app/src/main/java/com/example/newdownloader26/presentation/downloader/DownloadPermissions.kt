package com.example.newdownloader26.presentation.downloader

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.newdownloader26.R

@Composable
fun rememberDownloadActionWithPermissions(onDownload: () -> Unit): () -> Unit {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result.values.all { it }
        if (granted) {
            onDownload()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.permission_storage_needed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    return remember(context, permissionLauncher, onDownload) {
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                onDownload()
            } else {
                val writeGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                val readGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED

                if (writeGranted && readGranted) {
                    onDownload()
                } else {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    )
                }
            }
        }
    }
}
