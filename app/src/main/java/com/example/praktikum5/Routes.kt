package com.example.praktikum5

object Routes {
    const val HOME = "home"
    const val PROFILE = "profile"
    const val DETAIL = "detail/{studentId}"
    const val ABOUT = "about/{studentId}"
    const val FAVORITES = "favorites"
    const val LOCATION = "location"
    fun detail(studentId: Int): String = "detail/$studentId"
    fun about(studentId: Int): String = "about/$studentId"
}
