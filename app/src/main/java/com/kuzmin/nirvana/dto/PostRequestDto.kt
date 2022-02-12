package com.kuzmin.nirvana.dto

data class PostRequestDto(val id: Long,
                          val author: String,
                          val date: Long = 0,
                          val txt: String,
                          var like: Boolean = false,
                          var dislike: Boolean = false,
                          var liketxt: Int = 0,
                          var disliketxt: Int = 0,
                          val dateRepost: Long? = null,
                          val authorReposts: String? = null,
                          val url: String = "",
                          val authorId: Long) {
}