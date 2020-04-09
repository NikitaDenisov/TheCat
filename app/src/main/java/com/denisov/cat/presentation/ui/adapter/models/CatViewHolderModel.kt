package com.denisov.cat.presentation.ui.adapter.models

import com.denisov.cat.data.dto.Cat
import com.denisov.cat.presentation.ui.adapter.ViewHolderModel
import com.denisov.cat.presentation.ui.adapter.ViewHolderModels

class CatViewHolderModel(
    val cat: Cat,
    override val type: Int = ViewHolderModels.Cat
) : ViewHolderModel