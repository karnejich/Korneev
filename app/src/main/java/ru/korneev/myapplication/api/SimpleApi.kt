package ru.korneev.myapplication.api

import ru.korneev.myapplication.DevelopJson
import retrofit2.http.GET

interface SimpleApi {

    @GET("random?json=true")
    suspend fun getPost(): DevelopJson
}