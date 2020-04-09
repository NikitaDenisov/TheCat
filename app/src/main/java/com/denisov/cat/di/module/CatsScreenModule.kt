package com.denisov.cat.di.module

import com.denisov.cat.presentation.presenters.CatsPresenter
import com.denisov.cat.presentation.ui.adapter.ViewHolderFactory
import com.denisov.cat.presentation.ui.adapter.ViewHolderModels
import com.denisov.cat.presentation.ui.adapter.viewholders.CatViewHolder
import com.denisov.cat.presentation.ui.adapter.viewholders.MoreLoadingViewHolder
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntKey
import dagger.multibindings.IntoMap

@Module(includes = [CatsScreenModule.BindsModule::class])
class CatsScreenModule {

    @Module
    interface BindsModule {

        @Binds
        @IntoMap
        @IntKey(ViewHolderModels.Cat)
        fun bindsCatsViewHolderModel(factory: CatViewHolder.Factory): ViewHolderFactory

        @Binds
        @IntoMap
        @IntKey(ViewHolderModels.MoreLoading)
        fun bindsLoadingViewHolderModel(factory: MoreLoadingViewHolder.Factory): ViewHolderFactory

        @Binds
        fun bindCatItemListener(presenter: CatsPresenter): CatViewHolder.CatItemListener
    }
}