package com.denisov.cat.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.denisov.cat.data.entity.CatEntity
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface CatsDao {

    @Query("SELECT * FROM cat")
    fun getAll(): List<CatEntity>

    @Query("SELECT * FROM cat")
    fun getAllObservable(): Observable<List<CatEntity>>

    @Insert
    fun insert(cat: CatEntity): Completable

    @Delete
    fun delete(cat: CatEntity): Completable

    @Query("DELETE FROM cat")
    fun deleteAll()
}