package com.example.newdownloader26.presentation.downloader

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.newdownloader26.R
import com.example.newdownloader26.domain.model.DownloadPhase

@Composable
fun DownloadProgressDialog(
    state: DownloaderState,
    onDismiss: () -> Unit,
    onViewDownloads: () -> Unit = {}
) {
    val phase = state.downloadPhase
    val isRunning = state.isLoading
    val isCompleted = !isRunning && phase == DownloadPhase.COMPLETED

    if (isCompleted) {
        DownloadCompleteDialog(
            onViewDownloads = onViewDownloads,
            onContinue = onDismiss,
            onDismissRequest = onDismiss
        )
        return
    }

    val title = when (phase) {
        DownloadPhase.PREPARING -> stringResource(R.string.download_phase_preparing)
        DownloadPhase.DOWNLOADING -> stringResource(R.string.download_phase_downloading)
        DownloadPhase.SAVING -> stringResource(R.string.download_phase_saving)
        DownloadPhase.FAILED -> stringResource(R.string.download_phase_failed)
        DownloadPhase.COMPLETED -> stringResource(R.string.download_phase_completed)
        null -> stringResource(R.string.download_phase_download)
    }

    AlertDialog(
        onDismissRequest = { if (!isRunning) onDismiss() },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val message = state.statusMessageRes?.let { stringResource(it) } ?: state.statusMessage
                if (message.isNotBlank()) {
                    Text(text = message)
                }
                when {
                    isRunning && state.progressPercent != null -> {
                        LinearProgressIndicator(
                            progress = { state.progressPercent / 100f },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text("${state.progressPercent}%")
                    }
                    isRunning -> CircularProgressIndicator()
                    state.savedPath != null -> {
                        Text(
                            text = stringResource(R.string.download_saved_to_format, state.savedPath ?: ""),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (!isRunning) {
                TextButton(onClick = onDismiss) { Text(stringResource(R.string.common_ok)) }
            }
        }
    )
}

@Composable
private fun DownloadCompleteDialog(
    onViewDownloads: () -> Unit,
    onContinue: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val cardShape = RoundedCornerShape(50.dp)
    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false,
        dismissOnBackPress = false, dismissOnClickOutside = false)) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Transparent),
//            contentAlignment = Alignment.Center
//        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .fillMaxWidth()
                    .background(Color.White, cardShape)
                    .padding(horizontal = 26.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_download_complete),
                    contentDescription = null,
                    modifier = Modifier.size(110.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.download_complete_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF171717)
                )
                Text(
                    text = stringResource(R.string.download_complete_subtitle),
                    fontSize = 14.sp,
                    color = Color(0xFFB7B7B7),
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center

                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        onViewDownloads()
                        onContinue()
                    },
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF22B29)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text(
                        text = stringResource(R.string.download_complete_view_downloads),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                Button(
                    onClick = onContinue,
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .border(1.dp, Color(0xFFE9E9E9), RoundedCornerShape(28.dp))
                ) {
                    Text(
                        text = stringResource(R.string.common_continue),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF171717)
                    )
                }
            }
//        }
    }
}
