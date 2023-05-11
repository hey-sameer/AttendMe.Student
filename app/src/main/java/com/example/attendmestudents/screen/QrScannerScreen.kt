package com.example.attendmestudents.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendmestudents.qrScanner.QrCodeScanner
import com.example.attendmestudents.viewmodel.QRScannerViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QrScannerScreen(viewModel : QRScannerViewModel,navHostController: NavHostController, context: Context) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )
    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }
    Surface() {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if(hasCamPermission){
                AndroidView(
                    factory = { context ->
                        val previewView = PreviewView(context)
                        val preview = Preview.Builder().build()
                        val selector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()
                        preview.setSurfaceProvider(previewView.surfaceProvider)
                        val imageAnalysis =
                            ImageAnalysis.Builder()
                                .setTargetResolution(
                                    Size(
                                        previewView.width,
                                        previewView.height
                                    )
                                )
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()
                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            QrCodeScanner.QrScannerAnalyzer { result ->
                                if(!viewModel.isAttendanceInProgress.value)
                                    viewModel.dataFromQR(result, onSuccess = {Toast.makeText(context,"Attendance mark successfully!", Toast.LENGTH_LONG).show();navHostController.popBackStack()}){
                                    when(viewModel.errorCode.value){
                                        1 -> Toast.makeText(context,"Attendance not marked: Invalid QR", Toast.LENGTH_SHORT).show()
                                        2 -> Toast.makeText(context,"Attendance not marked: Expired QR", Toast.LENGTH_SHORT).show()
                                        5 -> Toast.makeText(context,"Attendance not marked: Network error(firebase)", Toast.LENGTH_SHORT).show()
                                        6 -> Toast.makeText(context,"Attendance not marked: Not registered for the class", Toast.LENGTH_SHORT).show()
                                    }
                                        navHostController.popBackStack()
                                }
                            }
                        )
                        try {
                            cameraProviderFuture.get().bindToLifecycle(
                                lifecycleOwner,
                                selector,
                                preview,
                                imageAnalysis
                            )

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        previewView
                    },
                    modifier = Modifier.weight(1f)
                )

            }

        }
    }
}