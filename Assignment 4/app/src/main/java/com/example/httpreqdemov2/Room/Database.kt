package com.example.httpreqdemov2.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.httpreqdemov2.FunFact

@Database(entities = [FunFact::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun factDao(): FactDaoInterface
}
