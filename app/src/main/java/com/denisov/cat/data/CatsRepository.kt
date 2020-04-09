package com.denisov.cat.data

import com.denisov.cat.data.api.CatsApi
import com.denisov.cat.data.database.CatsDao
import com.denisov.cat.data.dto.Cat
import com.denisov.cat.data.dto.mapToCatDto
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
                catsDao
                    .getAll()
                    .takeIf { it.isNotEmpty() }
                    ?.let { favoriteCats ->
                        cats.map { cat ->
                            cat.mapToCatDto(
                                favoriteCats.firstOrNull { it.id == cat.id } != null
                            )
                        }
                    }
                    ?: cats.map { it.mapToCatDto() }
            }

    fun getFavoriteCats(): Single<List<Cat>> =
        Single
            .fromCallable {
                catsDao.getAll().map { it.mapToCatDto(true) }
            }
}