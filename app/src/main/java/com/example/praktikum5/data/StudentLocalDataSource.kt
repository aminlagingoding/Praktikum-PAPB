package com.example.praktikum5.data

class StudentLocalDataSource {
    private val students = listOf(
        Student(2026, "Alya Putri", "alya@example.com", "Universitas Brawijaya"),
        Student(2027, "Bima Pratama", "bima@example.com", "Universitas Brawijaya"),
        Student(2028, "Citra Lestari", "citra@example.com", "Universitas Brawijaya"),
        Student(2029, "Deni Kurniawan", "deni@example.com", "Universitas Brawijaya"),
        Student(2030, "Eka Rahmawati", "eka@example.com", "Universitas Brawijaya")
    )

    fun getStudents(): List<Student> = students

    fun getStudentById(id: Int): Student? =
        students.find { it.id == id }
}
