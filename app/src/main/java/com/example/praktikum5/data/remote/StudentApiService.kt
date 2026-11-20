package com.example.praktikum5.data.remote

import com.example.praktikum5.data.remote.dto.UserDto
import com.example.praktikum5.data.remote.dto.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StudentApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("limit") limit: Int = 10
    ): UserResponse

    @GET("users/search")
    suspend fun searchUsers(
        @Query("q") query: String
    ): UserResponse

    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): UserDto
}
