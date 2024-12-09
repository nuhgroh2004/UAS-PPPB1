package com.example.uaspppb1.Model

import com.google.gson.annotations.SerializedName

data class Mood(
    @SerializedName("_id") val id: String? = null,
    val mood: String,
    val timestamp: String
)