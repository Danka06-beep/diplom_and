package com.kuzmin.nirvana.repository

import android.graphics.Bitmap
import com.kuzmin.nirvana.api.*
import com.kuzmin.nirvana.model.PostModel
import retrofit2.Response

class NetworkRepository(private val api: Api) : Repository {
    private var token: String? = null
    override suspend fun getPosts(): Response<List<PostModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun likedByMe(id: Long): Response<PostModel> {
        TODO("Not yet implemented")
    }

    override suspend fun cancelMyLike(id: Long): Response<PostModel> {
        TODO("Not yet implemented")
    }

    override suspend fun createPost(
        content: String,
        attachmentModel: PostModel.AttachmentModel?,
    ): Response<Void> {
        TODO("Not yet implemented")
    }

    override suspend fun createRepost(content: String, contentRepost: PostModel): Response<Void> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostsAfter(id: Long): Response<List<PostModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostsOld(id: Long): Response<List<PostModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun upload(bitmap: Bitmap): Response<PostModel.AttachmentModel> {
        TODO("Not yet implemented")
    }

    override suspend fun registerPushToken(token: String): Response<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostId(id: Long): Response<PostModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getMe(): Response<Me> {
        TODO("Not yet implemented")
    }

    override suspend fun tokenDeviceId(id: Long, tokenDevice: String): Response<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun authenticate(login: String, password: String): Response<Token> {
        token = api.authenticate(AuthRequestParams(username = login, password = password)).body()?.token
        return api.authenticate(AuthRequestParams(username = login, password = password))
    }

    override suspend fun register(login: String, password: String): Response<Token> =
        api.register(RegistrationRequestParams(username = login, password = password))
}