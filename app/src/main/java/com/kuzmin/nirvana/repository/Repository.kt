package com.kuzmin.nirvana.repository

import android.graphics.Bitmap
import com.kuzmin.nirvana.api.Me
import com.kuzmin.nirvana.api.Token
import com.kuzmin.nirvana.api.User
import com.kuzmin.nirvana.dto.AuthorPostResponseDto
import com.kuzmin.nirvana.dto.PasswordChangeRequestDto
import com.kuzmin.nirvana.model.PostModel
import retrofit2.Response

interface Repository {

    suspend fun getPosts(): Response<List<PostModel>>

    suspend fun likedByMe(id: Long): Response<PostModel>
    suspend fun dislike(id: Long): Response<PostModel>

    suspend fun createPost(content: String, attachmentModel: PostModel.AttachmentModel?): Response<Void>
    suspend fun createRepost(content: String, contentRepost:PostModel): Response<Void>
    suspend fun getPostsAfter(id: Long): Response<PostModel>
    suspend fun getPostsOld(id: Long): Response<List<PostModel>>

    suspend fun upload(bitmap: Bitmap): Response<PostModel.AttachmentModel>
    suspend fun uploadImageUser(bitmapUser: Bitmap): Response<PostModel.AttachmentModel>

    suspend fun changeImageUser(attachment: PostModel.AttachmentModel): Response<PostModel.AttachmentModel>

    suspend fun registerPushToken(token: String) : Response<User>

    suspend fun getPostId(id: Long): Response<PostModel>

    suspend fun getMe(): Response<Me>

    suspend fun tokenDeviceId(id: Long ,tokenDevice: String): Response<Boolean>

    suspend fun authenticate(login: String, password: String): Response<Token>

    suspend fun register(login: String, password: String): Response<Token>

    suspend fun changePassword(passwordChangeRequestDto: PasswordChangeRequestDto):  Response<AuthorPostResponseDto>

}