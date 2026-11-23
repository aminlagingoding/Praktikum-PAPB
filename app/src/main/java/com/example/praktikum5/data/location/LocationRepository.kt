package com.example.praktikum5.data.location

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<DeviceLocation>
}