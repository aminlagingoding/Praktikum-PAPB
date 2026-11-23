package com.example.praktikum5.ui.theme.Location

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LocationScreen(
    viewModel: LocationViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val coarse = result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val fine = result[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (coarse || fine) {
            viewModel.refreshLocation()
        } else {
            viewModel.onPermissionDenied()
        }
    }

    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Informasi Lokasi Perangkat", style = MaterialTheme.typography.headlineMedium)

        Button(
            onClick = {
                if (hasLocationPermission) {
                    viewModel.refreshLocation()
                } else {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (hasLocationPermission) "Perbarui Lokasi" else "Ambil Lokasi Saya")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                LocationUiState.Idle -> {
                    Text("Tekan tombol di atas untuk mengambil lokasi.")
                }
                LocationUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is LocationUiState.Success -> {
                    val location = state.location
                    val accessType = if (location.isPrecise) "Precise" else "Approximate"
                    val formattedTime = SimpleDateFormat("HH:mm:ss - dd MMM yyyy", Locale.getDefault())
                        .format(Date(location.timestamp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Jenis Akses: $accessType", style = MaterialTheme.typography.titleMedium)
                            Text("Latitude: ${location.latitude}", style = MaterialTheme.typography.bodyMedium)
                            Text("Longitude: ${location.longitude}", style = MaterialTheme.typography.bodyMedium)
                            Text("Akurasi: ${location.accuracy?.let { "%.2f m".format(it) } ?: "Tidak tersedia"}", style = MaterialTheme.typography.bodyMedium)
                            Text("Waktu Diperbarui: $formattedTime", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                is LocationUiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                        Button(
                            onClick = {
                                if (hasLocationPermission) {
                                    viewModel.refreshLocation()
                                } else {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_COARSE_LOCATION,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        )
                                    )
                                }
                            }
                        ) {
                            Text("Coba Lagi")
                        }
                    }
                }
                is LocationUiState.PermissionDenied -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(
                            onClick = {
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    )
                                )
                            }
                        ) {
                            Text("Minta Izin Lagi")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kembali")
        }
    }
}
