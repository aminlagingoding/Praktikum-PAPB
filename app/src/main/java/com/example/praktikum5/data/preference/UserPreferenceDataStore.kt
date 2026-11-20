package com.example.praktikum5.data.preference

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

internal val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)
