package com.denisov.cat.presentation.presenters

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
import com.denisov.cat.presentation.utils.subscribeWithoutSuspense
import com.denisov.cat.presentation.view.CatsView
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@PerFragment
class CatsPresenter @Inject constructor(
    private val catsRepository: CatsRepository,
    private val catsUseCases: CatsUseCases,
    private val schedulers: Schedulers,
    private val contextProvider: ContextProvider
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
    }

    override fun onCreate() {
        loadSubject.onNext(page++)
    }

    override fun onFavoriteClick(cat: Cat) {
        subscribe {
            when {
                cat.isFavorite -> catsUseCases.deleteCatFromFavorite(cat)
                else -> catsUseCases.addCatToFavorite(cat)
            }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.ui)
                .doOnSubscribe { view?.disableTouches() }
                .doAfterTerminate { view?.enableTouches() }
                .subscribe({
                    viewHolders
                        .indexOfFirst { it is CatViewHolderModel && it.cat.id == cat.id }
                        .takeIf { it > -1 }
                        ?.let { index ->
                            viewHolders[index] =
                                CatViewHolderModel(cat.copy(isFavorite = !cat.isFavorite))
                            view?.apply {
                                setViewHolderModels(viewHolders)
                                notifyItemChanged(index)
                            }
                        }
                }, {
                    view?.showError(contextProvider.getString(R.string.error))
                })
        }
    }

    override fun onDownloadClick(cat: Cat) {

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
                }
            }
            .doAfterTerminate { view?.hideProgress() }
            .doOnSuccess {
                viewHolders
                    .indexOfFirst { it is MoreLoadingViewHolderModel }
                    .takeIf { it > -1 }
                    ?.let { viewHolders.removeAt(it) }
                viewHolders.addAll(it)
                view?.setViewHolderModels(viewHolders)
            }
}