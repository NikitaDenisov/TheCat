package com.denisov.cat.presentation.presenters

import com.denisov.cat.data.CatsRepository
import com.denisov.cat.data.dto.Cat
import com.denisov.cat.di.scope.PerFragment
import com.denisov.cat.domain.CatsUseCases
import com.denisov.cat.presentation.BasePresenter
import com.denisov.cat.presentation.Schedulers
import com.denisov.cat.presentation.ui.adapter.ViewHolderModel
import com.denisov.cat.presentation.ui.adapter.models.CatViewHolderModel
import com.denisov.cat.presentation.ui.adapter.viewholders.CatViewHolder
import com.denisov.cat.presentation.view.FavoritesView
import javax.inject.Inject

@PerFragment
class FavoritesPresenter @Inject constructor(
    private val catsRepository: CatsRepository,
    private val schedulers: Schedulers,
    private val catsUseCases: CatsUseCases
) : BasePresenter<FavoritesView>(), CatViewHolder.CatItemListener {

    private val viewHolderModels = mutableListOf<ViewHolderModel>()

    override fun onCreate() {
        subscribe {
            catsRepository
                .getFavoriteCats()
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.ui)
                .subscribe({
                    it
                        .takeIf { it.isNotEmpty() }
                        ?.let {
                            viewHolderModels.addAll(it.map { CatViewHolderModel(it) })
                            view?.setViewHolderModels(viewHolderModels)
                        }
                        ?: run {
                            view?.showEmptyView()
                        }
                }, {
                    view?.showEmptyView()
                })
        }
    }

    override fun onFavoriteClick(cat: Cat) {
        subscribe {
            catsUseCases
                .deleteCatFromFavorite(cat)
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.ui)
                .subscribe({
                    viewHolderModels
                        .indexOfFirst { it is CatViewHolderModel && it.cat.id == cat.id }
                        .takeIf { it > -1 }
                        ?.let { index ->
                            viewHolderModels.removeAt(index)
                            view?.setViewHolderModels(viewHolderModels)
                            if (viewHolderModels.isEmpty()) {
                                view?.showEmptyView()
                            }
                        }
                }, {
                })
        }
    }

    override fun onDownloadClick(cat: Cat) {
    }
}