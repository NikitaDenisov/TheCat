package com.denisov.cat.data

import com.denisov.cat.data.api.CatsApi
import com.denisov.cat.data.database.CatsDao
import com.denisov.cat.data.dto.Cat
import com.denisov.cat.data.dto.mapToCatDto
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class CatsRepository @Inject constructor(
    private val catsDao: CatsDao,
    private val catsApi: CatsApi
) {

    fun getCats(page: Int): Single<List<Cat>> =
        catsApi
            .getCats(page)
            .map { cats ->
                cats.map { it.mapToCatDto() }
            }

    fun getFavoriteCats(): Observable<List<Cat>> =
        catsDao
            .getAllObservable()
            .map {
                it.map { it.mapToCatDto(true) }
            }
}