package com.kuzmin.nirvana.model

data class UserModel(
    val id: Long = 0,
    val username: String? = null,
    val userStatus: Long = 0,
    val attachment: PostModel.AttachmentModel? = null,
    val readOnly: Boolean = false,
    ){}
