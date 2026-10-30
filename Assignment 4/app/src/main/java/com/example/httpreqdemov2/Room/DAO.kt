package com.example.httpreqdemov2.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.httpreqdemov2.FunFact
import kotlinx.coroutines.flow.Flow

@Dao
interface FactDaoInterface {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFact(fact: FunFact)

    @Query("SELECT * FROM facts")
    fun getAllFacts(): Flow<List<FunFact>>
}
