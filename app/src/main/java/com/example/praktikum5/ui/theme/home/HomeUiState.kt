package com.example.praktikum5.ui.theme.home

import com.example.praktikum5.data.Student

data class HomeUiState(
    val students: List<Student> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
