package com.denisov.cat.presentation.ui.adapter.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denisov.cat.R
import com.denisov.cat.presentation.ui.adapter.BaseViewHolder
import com.denisov.cat.presentation.ui.adapter.ViewHolderFactory
import com.denisov.cat.presentation.ui.adapter.ViewHolderModel
import com.denisov.cat.presentation.ui.adapter.models.MoreLoadingViewHolderModel
import javax.inject.Inject

class MoreLoadingViewHolder(view: View) : BaseViewHolder<MoreLoadingViewHolderModel>(view) {

    override fun bind(model: MoreLoadingViewHolderModel) {
    }

    class Factory @Inject constructor(
        layoutInflater: LayoutInflater
    ) : ViewHolderFactory(layoutInflater) {

        override fun create(parent: ViewGroup): BaseViewHolder<out ViewHolderModel> =
            MoreLoadingViewHolder(
                layoutInflater.inflate(R.layout.item_more_loading, parent, false)
            )
    }
}