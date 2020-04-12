package com.denisov.cat.presentation.ui.adapter.viewholders

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.denisov.cat.R
import com.denisov.cat.data.dto.Cat
import com.denisov.cat.presentation.ui.adapter.BaseViewHolder
import com.denisov.cat.presentation.ui.adapter.ViewHolderFactory
import com.denisov.cat.presentation.ui.adapter.ViewHolderModel
import com.denisov.cat.presentation.ui.adapter.models.CatViewHolderModel
import com.denisov.cat.presentation.utils.show
import javax.inject.Inject

class CatViewHolder(
    view: View,
    listener: CatItemListener
) : BaseViewHolder<CatViewHolderModel>(view) {

    private var model: Cat? = null
    private val imageView: ImageView = view.findViewById(R.id.imageView)
    private val favoriteButton: ImageButton = view.findViewById(R.id.favoriteButton)
    private val downloadButton: View = view.findViewById(R.id.downloadButton)

    init {
        favoriteButton.setOnClickListener {
            model?.let {
                listener.onFavoriteClick(it)
            }
        }
        downloadButton.setOnClickListener {
            model?.let { cat ->
                (imageView.drawable as? BitmapDrawable)
                    ?.bitmap
                    ?.let { listener.onDownloadClick(cat, it) }
            }
        }
    }

    override fun bind(model: CatViewHolderModel) {
        this.model = model.cat
        val starDrawableResId = if (model.cat.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star
        favoriteButton.setImageDrawable(
            ContextCompat.getDrawable(context, starDrawableResId)
        )
        Glide
            .with(itemView.context)
            .load(model.cat.url)
            .placeholder(R.drawable.drawable_placeholder)
            .listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean = false

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    favoriteButton.show()
                    downloadButton.show()
                    return false
                }

            })
            .into(imageView)
    }

    interface CatItemListener {
        fun onFavoriteClick(cat: Cat)

        fun onDownloadClick(cat: Cat, bitmap: Bitmap)
    }

    class Factory @Inject constructor(
        layoutInflater: LayoutInflater,
        private val listener: CatItemListener
    ) : ViewHolderFactory(layoutInflater) {

        override fun create(parent: ViewGroup): BaseViewHolder<out ViewHolderModel> =
            CatViewHolder(
                layoutInflater.inflate(R.layout.item_cat, parent, false),
                listener
            )
    }
}