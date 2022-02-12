package com.kuzmin.nirvana.dto

import com.kuzmin.nirvana.model.PostModel

data class NewPostDto(
    val author: String? = null,
    val data: Long = 0,
    val txt: String? = null,
    val adress : String? = null,
    val coordinates : Pair<Double,Double>? = null,
    val type: PostModel.PostTypes = PostModel.PostTypes.REPOST
) {
}