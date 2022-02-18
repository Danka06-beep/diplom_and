package com.kuzmin.nirvana.dto

import com.kuzmin.nirvana.model.PostModel

data class AuthorPostResponseDto(
    val id: Long = 0,
    val username: String,
    val attachment: PostModel.AttachmentModel? = null,
    val readOnly: Boolean = false
) {
}