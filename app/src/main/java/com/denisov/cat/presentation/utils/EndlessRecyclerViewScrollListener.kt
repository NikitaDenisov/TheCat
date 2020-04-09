package com.denisov.cat.presentation.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class EndlessRecyclerViewScrollListener(
    private val layoutManager: RecyclerView.LayoutManager,
    loadMore: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var visibleThreshold = 5
    private var previousTotalItemCount = 0
    private var loading = true
    private val loadMoreRunnable = LoadMoreRunnable(loadMore)

    init {
        visibleThreshold *= when (layoutManager) {
            is StaggeredGridLayoutManager -> layoutManager.spanCount
            is GridLayoutManager -> layoutManager.spanCount
            else -> 1
        }
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = layoutManager.itemCount

        when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> lastVisibleItemPosition =
                layoutManager.findLastVisibleItemPosition()
            is LinearLayoutManager -> lastVisibleItemPosition =
                layoutManager.findLastVisibleItemPosition()
        }

        if (totalItemCount < previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            view.post(loadMoreRunnable)
            loading = true
        }
    }

    fun resetState() {
        this.previousTotalItemCount = 0
        this.loading = true
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    class LoadMoreRunnable(private val loadMore: () -> Unit) : Runnable {
        override fun run() {
            loadMore()
        }
    }
}