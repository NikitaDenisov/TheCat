package com.denisov.cat.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

@Suppress("AddVarianceModifier")
abstract class BaseViewHolder<T : ViewHolderModel>(
    override val view: View
) : RecyclerView.ViewHolder(view), ViewBinder<T>

abstract class ViewHolderFactory(val layoutInflater: LayoutInflater) {
    abstract fun create(parent: ViewGroup): BaseViewHolder<out ViewHolderModel>
}

interface ViewHolderModel {
    val type: Int

    infix fun isTheSameAs(otherViewHolderModel: ViewHolderModel) = this == otherViewHolderModel

    infix fun hasSameContentAs(otherViewHolderModel: ViewHolderModel) = this == otherViewHolderModel
}

interface ViewBinder<T> {
    val view: View
    val context: Context get() = view.context

    fun bind(model: T)
}