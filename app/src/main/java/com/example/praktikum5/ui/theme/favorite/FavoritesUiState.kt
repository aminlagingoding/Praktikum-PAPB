package com.example.praktikum5.ui.theme.favorite

import com.example.praktikum5.data.local.FavoriteStudentEntity

data class FavoritesUiState(
    val favorites: List<FavoriteStudentEntity> = emptyList(),
    val isLoading: Boolean = false
)
