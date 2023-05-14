package com.example.attendmestudents.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.attendmestudents.R
import com.example.attendmestudents.viewmodel.QRScannerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QrScannerScreen(viewModel : QRScannerViewModel, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val showText = viewModel.errorMessage.value
    Surface(modifier = Modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 32.dp)
                    .weight(3f),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Scan QR Code",
                        style = MaterialTheme.typography.titleLarge.copy(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                    )
                    Text(
                        text = showText,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center)
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.qr_code),
                contentDescription = "qr",
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .weight(6f)
            )
            Button(
                onClick = {
                    scope.launch(Dispatchers.Main) {
                        viewModel.startScan(
                            onSuccess = {},
                            onFailure = {},
                        )
                    }
                },
                modifier = Modifier
            ) {
                Text(text = "SCAN", modifier = Modifier.widthIn(min = 18.dp), style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
