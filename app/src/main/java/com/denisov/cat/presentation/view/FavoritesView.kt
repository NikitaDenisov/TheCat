package com.denisov.cat.presentation.view

import com.denisov.cat.presentation.BaseView
import com.denisov.cat.presentation.ui.adapter.ViewHolderModel

interface FavoritesView : BaseView {

    fun setViewHolderModels(viewHolderModels: List<ViewHolderModel>)

    fun showEmptyView(show: Boolean)
}