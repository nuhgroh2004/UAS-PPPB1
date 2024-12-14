package com.example.uaspppb1.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val id_user: String,
    val username: String,
    val password: String,
    val email: String
)