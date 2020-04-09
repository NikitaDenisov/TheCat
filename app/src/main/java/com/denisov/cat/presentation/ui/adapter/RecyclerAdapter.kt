package com.denisov.cat.presentation.ui.adapter

import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

typealias ViewHolderFactories = MutableMap<Int, ViewHolderFactory>

@MainThread
open class RecyclerAdapter @Inject constructor(private val factories: ViewHolderFactories) :
    RecyclerView.Adapter<BaseViewHolder<ViewHolderModel>>() {

    var viewModels = listOf<ViewHolderModel>()
        private set

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewHolderModel> {
        return (factories[viewType]?.create(parent)
            ?: throw NullPointerException("View holder for viewType=$viewType not found")
                ) as BaseViewHolder<ViewHolderModel>
    }

    override fun getItemViewType(position: Int) = viewModels[position].type

    override fun getItemCount() = viewModels.size

    override fun onBindViewHolder(holder: BaseViewHolder<ViewHolderModel>, position: Int) {
        holder.bind(viewModels[position])
    }

    fun setModels(newViewModels: List<ViewHolderModel>) {
        updateModels(newViewModels)
    }

    private fun updateModels(viewModelsList: List<ViewHolderModel>) {
        DiffUtil.calculateDiff(createDiffCallback(viewModels, viewModelsList), false)
            .let { result ->
                viewModels = viewModelsList
                result.dispatchUpdatesTo(this)
            }
    }

    private fun createDiffCallback(
        oldList: List<ViewHolderModel>,
        newList: List<ViewHolderModel>
    ) = object : DiffUtil.Callback() {

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ) = oldList[oldItemPosition] isTheSameAs newList[newItemPosition]

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ) = oldList[oldItemPosition] hasSameContentAs newList[newItemPosition]
    }
}