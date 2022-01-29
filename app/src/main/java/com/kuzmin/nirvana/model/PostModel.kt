package com.kuzmin.nirvana.model

import com.kuzmin.nirvana.BASE_URL

 data class PostModel(val id: Long = 0,
                      val author: String? = null,
                      val data: Long = 0,
                      var txt: String? = null,
                      var like: Boolean = false,
                      val comment: Boolean = false,
                      var share: Boolean = false,
                      var likeTxt: Int = 0,
                      val commentTxt: Int = 0,
                      var shareTxt: Int = 0,
                      val adress: String? = null,
                      val coordinates: Pair<Double, Double>? = null,
                      val type: PostTypes = PostTypes.REPOST,
                      val url: String? = null,
                      val dateRepost: Long? = null,
                      val autorRepost: String? = null,
                      var hidePost: Boolean = false,
                      var viewPost: Long = 0,
                      val repost: PostModel? = null,
                      val attachment: AttachmentModel?) {

     var likeActionPerforming = false
     var repostActionPerforming = false


     fun updatePost(updatedModel: PostModel) {
         if (id != updatedModel.id) throw IllegalAccessException("Идентификаторы разные")
         likeTxt = updatedModel.likeTxt
         like = updatedModel.like
         txt = updatedModel.txt
         share = updatedModel.share
         shareTxt = updatedModel.shareTxt

     }

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