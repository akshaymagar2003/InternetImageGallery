package com.example.api.Network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
val retrofit = Retrofit.Builder()
    .baseUrl("https://api.flickr.com")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(ApiService::class.java)
interface ApiService {
    @GET("/services/rest")
    suspend fun getRecentPhotos(
        @Query("method") method: String = "flickr.photos.getRecent",
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1,
        @Query("extras") extras: String = "url_s"
    ): Response<Assignment>
}