package com.denisov.cat.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cat")
data class CatEntity(
    @PrimaryKey
    val id: String,
    val height: Int,
    val width: Int,
    val url: String
) : Serializable