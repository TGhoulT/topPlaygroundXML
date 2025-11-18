package com.example.topplaygroundxml.features.auth.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.topplaygroundxml.features.auth.data.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUser(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Insert
    suspend fun insertUser(user: User): Long
}