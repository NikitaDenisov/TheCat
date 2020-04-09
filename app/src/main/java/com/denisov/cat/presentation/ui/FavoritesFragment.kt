package com.denisov.cat.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.denisov.cat.R
import com.denisov.cat.di.component.buildFavoriteComponent
import com.denisov.cat.presentation.presenters.FavoritesPresenter
import com.denisov.cat.presentation.ui.adapter.RecyclerAdapter
import com.denisov.cat.presentation.ui.adapter.ViewHolderModel
import com.denisov.cat.presentation.utils.show
import com.denisov.cat.presentation.view.FavoritesView
import kotlinx.android.synthetic.main.fragment_cats.*
import kotlinx.android.synthetic.main.fragment_cats.recyclerView
import kotlinx.android.synthetic.main.fragment_favorites.*
import javax.inject.Inject

class FavoritesFragment : Fragment(), FavoritesView {

    private val component by buildFavoriteComponent()

    @Inject
    lateinit var presenter: FavoritesPresenter
    @Inject
    lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)

        viewLifecycleOwnerLiveData.observe(this, Observer {
            it.lifecycle.addObserver(presenter)
            presenter.attachView(this)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            val gridLayoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            adapter = recyclerAdapter
            layoutManager = gridLayoutManager
            itemAnimator = null
        }
    }

    override fun setViewHolderModels(viewHolderModels: List<ViewHolderModel>) {
        recyclerAdapter.apply {
            setModels(viewHolderModels)
            notifyDataSetChanged()
        }
    }

    override fun showEmptyView() {
        emptyView.show()
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}