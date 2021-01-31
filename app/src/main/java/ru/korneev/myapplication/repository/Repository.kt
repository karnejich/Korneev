package ru.korneev.myapplication.repository

import ru.korneev.myapplication.DevelopJson
import ru.korneev.myapplication.api.RetrofitInstance

class Repository {

    suspend fun getPost(): DevelopJson {
        return RetrofitInstance.api.getPost()
    }

}