package com.denisov.cat.presentation.presenters

import android.graphics.Bitmap
import com.denisov.cat.R
import com.denisov.cat.data.CatsRepository
import com.denisov.cat.data.dto.Cat
import com.denisov.cat.di.scope.PerFragment
import com.denisov.cat.domain.CatsUseCases
import com.denisov.cat.presentation.BasePresenter
import com.denisov.cat.presentation.Schedulers
import com.denisov.cat.presentation.ui.adapter.ViewHolderModel
import com.denisov.cat.presentation.ui.adapter.models.CatViewHolderModel
import com.denisov.cat.presentation.ui.adapter.models.MoreLoadingViewHolderModel
import com.denisov.cat.presentation.ui.adapter.viewholders.CatViewHolder
import com.denisov.cat.presentation.utils.ContextProvider
import com.denisov.cat.presentation.utils.ImageDownloadManager
import com.denisov.cat.presentation.utils.subscribeWithoutSuspense
import com.denisov.cat.presentation.view.CatsView
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@PerFragment
class CatsPresenter @Inject constructor(
    private val catsRepository: CatsRepository,
    private val catsUseCases: CatsUseCases,
    private val schedulers: Schedulers,
    private val contextProvider: ContextProvider,
    private val downloadManager: ImageDownloadManager
) : BasePresenter<CatsView>(), CatViewHolder.CatItemListener {

    @Volatile
    private var page = 0
    @Volatile
    private var loading = false

    private val loadSubject = PublishSubject.create<Int>()
    private val viewHolders = mutableListOf<ViewHolderModel>()

    init {
        subscribe {
            loadSubject
                .doOnNext { loading = true }
                .switchMap {
                    load(it)
                        .doAfterTerminate { loading = false }
                        .toObservable()
                }
                .retry()
                .subscribeWithoutSuspense()
        }
        subscribe {
            catsRepository
                .getFavoriteCats()
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.ui)
                .subscribe({ favorites ->
                    mergeLists(favorites)
                }, {})
        }
    }

    override fun onCreate() {
        onLoadMore()
    }

    override fun onFavoriteClick(cat: Cat) {
        subscribe {
            when {
                cat.isFavorite -> catsUseCases.deleteCatFromFavorite(cat)
                else -> catsUseCases.addCatToFavorite(cat)
            }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.ui)
                .subscribe({
                }, {
                    view?.showError(contextProvider.getString(R.string.error))
                })
        }
    }

    override fun onDownloadClick(cat: Cat, bitmap: Bitmap) {
        downloadManager.download(Pair(cat, bitmap))
    }

    fun onLoadMore() {
        if (!loading) {
            loadSubject.onNext(page++)
        }
    }

    private fun load(page: Int) =
        catsRepository
            .getCats(page)
            .map { it.map { CatViewHolderModel(it) } }
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
            .doOnSubscribe {
                if (viewHolders.isEmpty()) {
                    view?.showProgress()
                } else {
                    viewHolders.add(MoreLoadingViewHolderModel())
                    view?.setViewHolderModels(viewHolders)
                }
            }
            .doAfterTerminate { view?.hideProgress() }
            .doOnSuccess {
                viewHolders
                    .indexOfLast { it is MoreLoadingViewHolderModel }
                    .takeIf { it > -1 }
                    ?.let { viewHolders.removeAt(it) }
                viewHolders.addAll(it)
                view?.setViewHolderModels(viewHolders)
            }

    private fun mergeLists(favorites: List<Cat>) {
        viewHolders
            .filterIsInstance<CatViewHolderModel>()
            .map { it.cat }
            .let { cats ->
                val changedIndexes = mutableListOf<Int>()
                val sortedFavorites = favorites.toHashSet()

                cats.forEachIndexed { index, cat ->
                    if (cat.isFavorite && !sortedFavorites.contains(cat)) {
                        changedIndexes.add(index)
                        viewHolders[index] = CatViewHolderModel(cat.copy(isFavorite = false))
                    } else if (!cat.isFavorite && sortedFavorites.contains(cat)) {
                        changedIndexes.add(index)
                        viewHolders[index] = CatViewHolderModel(cat.copy(isFavorite = true))
                    }
                }

                view?.apply {
                    setViewHolderModels(viewHolders)
                    changedIndexes.forEach { notifyItemChanged(it) }
                }
            }
    }
}