package com.example.httpreqdemov2

import android.app.Application
import androidx.room.Room
import com.example.httpreqdemov2.Room.AppDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class FactApp : Application() {

    val client by lazy {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "AppDatabase"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    val repository by lazy {
        Repository(db.factDao(), client)
    }
}
