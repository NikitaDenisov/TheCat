package com.denisov.cat.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.denisov.cat.R
import com.denisov.cat.di.component.buildCatsComponent
import com.denisov.cat.presentation.presenters.CatsPresenter
import com.denisov.cat.presentation.ui.adapter.RecyclerAdapter
import com.denisov.cat.presentation.ui.adapter.ViewHolderModel
import com.denisov.cat.presentation.utils.*
import com.denisov.cat.presentation.view.CatsView
import kotlinx.android.synthetic.main.fragment_cats.*
import javax.inject.Inject

class CatsFragment : Fragment(), CatsView {

    private val component by buildCatsComponent()

    @Inject
    lateinit var presenter: CatsPresenter
    @Inject
    lateinit var recyclerAdapter: RecyclerAdapter

    private lateinit var loadMoreListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        viewLifecycleOwnerLiveData.observe(this, Observer {
            presenter.attachView(this)
            it.lifecycle.addObserver(presenter)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            val gridLayoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)

            adapter = recyclerAdapter
            layoutManager = gridLayoutManager
            itemAnimator = null
            loadMoreListener = EndlessRecyclerViewScrollListener(gridLayoutManager) {
                presenter.onLoadMore()
            }
            addOnScrollListener(loadMoreListener)
        }
    }

    override fun setViewHolderModels(viewHolderModels: List<ViewHolderModel>) {
        recyclerAdapter.setModels(viewHolderModels)
    }

    override fun notifyItemChanged(position: Int) {
        recyclerAdapter.notifyItemChanged(position)
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun disableTouches() {
        activity?.disableTouches()
    }

    override fun enableTouches() {
        activity?.enableTouches()
    }

    override fun showProgress() {
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
    }

    companion object {
        fun newInstance() = CatsFragment()
    }
}