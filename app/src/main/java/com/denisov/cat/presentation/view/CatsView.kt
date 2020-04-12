package com.denisov.cat.presentation.view

import com.denisov.cat.presentation.BaseView
import com.denisov.cat.presentation.ui.adapter.ViewHolderModel

interface CatsView : BaseView {
    fun setViewHolderModels(viewHolderModels: List<ViewHolderModel>)

    fun notifyItemChanged(position: Int)

    fun showError(message: String)

    fun showProgress()

    fun hideProgress()
}