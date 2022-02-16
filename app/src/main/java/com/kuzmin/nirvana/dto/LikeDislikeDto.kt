package com.kuzmin.nirvana.dto

data class LikeDislikeDto(val author: AuthorPostResponseDto,val date: Long, val type: LikeType) {
}
enum class LikeType(){
    Like,
    Dislike
}