package com.example.httpreqdemov2

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity(tableName = "facts")
data class FunFact(
    val text: String,
    @PrimaryKey(autoGenerate = true)
    @Transient
    val id: Int = 0
)
