package com.denisov.cat.domain

import com.denisov.cat.data.database.CatsDao
import com.denisov.cat.data.dto.Cat
import com.denisov.cat.data.dto.mapToCatEntity
import io.reactivex.Completable
import javax.inject.Inject

class CatsUseCases @Inject constructor(private val catsDao: CatsDao) {

    fun addCatToFavorite(cat: Cat): Completable =
        Completable.fromCallable { catsDao.insert(cat.mapToCatEntity()) }

    fun deleteCatFromFavorite(cat: Cat): Completable =
        Completable.fromCallable { catsDao.delete(cat.mapToCatEntity()) }

    fun clearFavorites(): Completable = Completable.fromCallable { catsDao.deleteAll() }
}