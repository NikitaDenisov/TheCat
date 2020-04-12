package com.denisov.cat.data.dto

import com.denisov.cat.data.entity.CatEntity
import java.io.Serializable

data class Cat(
    val id: String,
    val height: Int,
    val width: Int,
    val url: String,
    val isFavorite: Boolean
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cat

        if (id != other.id) return false
        if (height != other.height) return false
        if (width != other.width) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + height
        result = 31 * result + width
        result = 31 * result + url.hashCode()
        return result
    }
}

fun CatEntity.mapToCatDto(isFavorite: Boolean = false) = Cat(
    id,
    height,
    width,
    url,
    isFavorite
)

fun Cat.mapToCatEntity() = CatEntity(id, height, width, url)