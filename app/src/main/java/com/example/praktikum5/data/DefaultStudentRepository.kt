package com.example.praktikum5.data

import com.example.praktikum5.data.remote.StudentRemoteDataSource
import com.example.praktikum5.data.remote.dto.toStudent

class DefaultStudentRepository(
    private val remoteDataSource: StudentRemoteDataSource
) : StudentRepository {
    override suspend fun getStudents(): List<Student> =
        remoteDataSource.getStudents().map { it.toStudent() }
    override suspend fun getStudent(id: Int): Student =
        remoteDataSource.getStudent(id).toStudent()
    override suspend fun searchStudents(query: String): List<Student> =
        remoteDataSource.searchStudents(query).map { it.toStudent() }
}
