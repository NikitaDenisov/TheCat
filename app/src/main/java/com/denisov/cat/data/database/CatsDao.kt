package com.denisov.cat.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.denisov.cat.data.entity.CatEntity

@Dao
interface CatsDao {

    @Query("SELECT * FROM cat")
    fun getAll(): List<CatEntity>

    @Insert
    fun insert(cat: CatEntity)

    @Delete
    fun delete(cat: CatEntity)

    @Query("DELETE FROM cat")
    fun deleteAll()
}