package com.denisov.cat.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.denisov.cat.data.entity.CatEntity

@Database(entities = [CatEntity::class], version = VERSION, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): CatsDao
}

const val DATABASE_NAME = "favorite_cats"
const val VERSION = 1