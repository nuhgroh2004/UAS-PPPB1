package com.example.uaspppb1.Api

import com.example.uaspppb1.Model.Mood
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("{database}/{table}")
    fun getAllData(
        @Path("database") database: String,
        @Path("table") table: String
    ): Call<List<Mood>>

    @POST("{database}/{table}")
    fun postData(
        @Path("database") database: String,
        @Path("table") table: String,
        @Body mood: Mood
    ): Call<Mood>
    @DELETE("{database}/{table}/{id}")
    fun deleteMood(
        @Path("database") database: String,
        @Path("table") table: String,
        @Path("id") id: String
    ): Call<Void>
}