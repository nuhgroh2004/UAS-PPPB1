package com.example.uaspppb1.Ui

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.uaspppb1.DataBase.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUser(email: String, password: String): User?

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): User?
}