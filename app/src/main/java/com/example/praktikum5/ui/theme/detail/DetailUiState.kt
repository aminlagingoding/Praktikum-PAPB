package com.example.praktikum5.ui.theme.detail

import com.example.praktikum5.data.Student

data class DetailUiState(
    val isLoading: Boolean = false,
    val student: Student? = null,
    val isFavorite: Boolean = false,
    val errorMessage: String? = null
)