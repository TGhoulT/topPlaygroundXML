package com.example.topplaygroundxml.features.auth.data.repository

import com.example.topplaygroundxml.features.auth.data.dao.UserDao
import com.example.topplaygroundxml.features.auth.data.entity.User
import com.example.topplaygroundxml.features.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun login(email: String, password: String): Boolean {
        return userDao.getUser(email, password) != null
    }

    override suspend fun register(email: String, password: String): Boolean {
        return try {
            val user = User(email = email, password = password)
            userDao.insertUser(user) > 0
        } catch (_: Exception) {
            false //Пользователь уже существует
        }
    }
}