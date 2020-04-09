package com.denisov.cat.di.module

import com.denisov.cat.presentation.presenters.FavoritesPresenter
import com.denisov.cat.presentation.ui.adapter.ViewHolderFactory
import com.denisov.cat.presentation.ui.adapter.ViewHolderModels
import com.denisov.cat.presentation.ui.adapter.viewholders.CatViewHolder
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntKey
import dagger.multibindings.IntoMap

@Module(includes = [FavoritesScreenModule.BindsModule::class])
class FavoritesScreenModule {

    @Module
    interface BindsModule {

        @Binds
        @IntoMap
        @IntKey(ViewHolderModels.Cat)
        fun bindsCatsViewHolderModel(factory: CatViewHolder.Factory): ViewHolderFactory

        @Binds
        fun bindCatItemListener(presenter: FavoritesPresenter): CatViewHolder.CatItemListener
    }
}