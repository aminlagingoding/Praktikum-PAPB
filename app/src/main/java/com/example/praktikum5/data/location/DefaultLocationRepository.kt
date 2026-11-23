package com.example.praktikum5.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class DefaultLocationRepository(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationRepository {
    override suspend fun getCurrentLocation(): Result<DeviceLocation> = runCatching {
        val fineGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        check(fineGranted || coarseGranted) { "Izin lokasi belum diberikan" }

        val priority = if (fineGranted) {
            Priority.PRIORITY_HIGH_ACCURACY
        } else {
            Priority.PRIORITY_BALANCED_POWER_ACCURACY
        }

        val location = client.getCurrentLocation(priority, null).await()
            ?: client.lastLocation.await()
            ?: error("Lokasi belum tersedia di perangkat")

        DeviceLocation(
            latitude = location.latitude,
            longitude = location.longitude,
            accuracy = if (location.hasAccuracy()) location.accuracy else null,
            isPrecise = fineGranted,
            timestamp = if (location.time > 0) location.time else System.currentTimeMillis()
        )
    }
}
