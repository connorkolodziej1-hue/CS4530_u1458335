package com.example.httpreqdemov2

import android.util.Log
import com.example.httpreqdemov2.Room.FactDaoInterface
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow

class Repository(
    private val dao: FactDaoInterface,
    private val client: HttpClient
) {
    val allFacts: Flow<List<FunFact>> = dao.getAllFacts()

    suspend fun fetchAndSaveFact() {
        try {
            val response: FunFact = client.get("https://uselessfacts.jsph.pl/api/v2/facts/random").body()
            dao.insertFact(response)
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching fact from network", e)
        }
    }
}
