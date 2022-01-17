package com.kuzmin.nirvana.model

import com.kuzmin.nirvana.BASE_URL

 class PostModel() {
    enum class PostTypes {
        POST, REPOST
    }

    enum class AttachmentType {
        IMAGE, AUDIO, VIDEO
    }

    data class AttachmentModel(val id: String, val mediaType: AttachmentType) {
        val url
            get() = "$BASE_URL/api/v1/static/$id"
    }
}