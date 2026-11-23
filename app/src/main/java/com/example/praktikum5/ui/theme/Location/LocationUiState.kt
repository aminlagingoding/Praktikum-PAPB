package com.example.praktikum5.ui.theme.Location

import com.example.praktikum5.data.location.DeviceLocation

sealed interface LocationUiState {
    data object Idle : LocationUiState
    data object Loading : LocationUiState
    data class Success(val location: DeviceLocation) : LocationUiState
    data class Error(val message: String) : LocationUiState
    data class PermissionDenied(val message: String = "Izin lokasi ditolak. Silakan izinkan akses lokasi untuk melihat informasi koordinat.") : LocationUiState
}
