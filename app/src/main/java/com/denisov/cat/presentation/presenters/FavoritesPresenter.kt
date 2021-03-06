package com.denisov.cat.presentation.presenters

import android.graphics.Bitmap
import com.denisov.cat.data.CatsRepository
import com.denisov.cat.data.dto.Cat
import com.denisov.cat.di.scope.PerFragment
import com.denisov.cat.domain.CatsUseCases
import com.denisov.cat.presentation.BasePresenter
import com.denisov.cat.presentation.Schedulers
import com.denisov.cat.presentation.ui.adapter.models.CatViewHolderModel
import com.denisov.cat.presentation.ui.adapter.viewholders.CatViewHolder
import com.denisov.cat.presentation.utils.ImageDownloadManager
import com.denisov.cat.presentation.view.FavoritesView
import javax.inject.Inject

@PerFragment
class FavoritesPresenter @Inject constructor(
    private val catsRepository: CatsRepository,
    private val schedulers: Schedulers,
    private val catsUseCases: CatsUseCases,
    private val downloadManager: ImageDownloadManager
) : BasePresenter<FavoritesView>(), CatViewHolder.CatItemListener {

    override fun onCreate() {
        subscribe {
            catsRepository
                .getFavoriteCats()
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.ui)
                .subscribe({ cats ->
                    view?.apply {
                        setViewHolderModels(
                            cats.map { CatViewHolderModel(it) }
                        )
                        showEmptyView(cats.isEmpty())
                    }
                }, {
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
                }, {
                })
        }
    }

    override fun onDownloadClick(cat: Cat, bitmap: Bitmap) {
        downloadManager.download(Pair(cat, bitmap))
    }
}