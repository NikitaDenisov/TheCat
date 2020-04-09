package com.denisov.cat.data.dto

import com.denisov.cat.data.entity.CatEntity
import java.io.Serializable

data class Cat(
    val id: String,
    val height: Int,
    val width: Int,
    val url: String,
    val isFavorite: Boolean
) : Serializable

fun CatEntity.mapToCatDto(isFavorite: Boolean = false) = Cat(
    id,
    height,
    width,
    url,
    isFavorite
)

fun Cat.mapToCatEntity() = CatEntity(id, height, width, url)