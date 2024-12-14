package com.example.uaspppb1.Api

import com.example.uaspppb1.Model.Mood
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // tidak kepakai
    @GET("{database}/{table}")
    fun getAllData(
        @Path("database") database: String,
        @Path("table") table: String
    ): Call<List<Mood>>

    // untuk riwayat
    @GET("{database}/{table}")
    fun getDataByUserIdAndRoom(
        @Path("database") database: String,
        @Path("table") table: String,
        @Query("id_user") id_user: String,
        @Query("id_room") id_room: String
    ): Call<List<Mood>>

    // untuk dibagian home
    @GET("{database}/{table}")
    fun getDataByUserId(
        @Path("database") database: String,
        @Path("table") table: String,
        @Query("id_user") id_user: String
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

    @POST("{database}/{table}/{id}")
    fun updateMood(
        @Path("database") database: String,
        @Path("table") table: String,
        @Path("id") id: String,
        @Body mood: Map<String, String>
    ): Call<Mood>
}