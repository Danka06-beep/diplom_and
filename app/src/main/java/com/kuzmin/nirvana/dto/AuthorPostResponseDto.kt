package com.kuzmin.nirvana.dto

import com.kuzmin.nirvana.model.PostModel

data class AuthorPostResponseDto(
    val id: Long = 0,
    val username: String,
    val attachment: PostModel.AttachmentUserModel? = null,
    val readOnly: Boolean = false
) {
}