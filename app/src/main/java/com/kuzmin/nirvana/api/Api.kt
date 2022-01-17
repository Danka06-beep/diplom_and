package com.kuzmin.nirvana.api

import com.kuzmin.nirvana.model.PostModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


data class AuthRequestParams(val username: String, val password: String)

data class Token(val token: String)

data class RegistrationRequestParams(val username: String, val password: String)

data class TokenDevice(val id: Long,val tokenDevice: String)

data class Me(val id: Long = 0,val name: String)

interface Api {

    @POST("api/v1/authentication")
    suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>

    @POST("api/v1/registration")
    suspend fun register(@Body registrationRequestParams: RegistrationRequestParams): Response<Token>
}

data class CreatePostRequest(
    val id: Long = 0,
    val txt: String,
    val attachment: PostModel.AttachmentModel? = null
)
data class CreateRepostRequest(val id: Long = 0, val txt: String, val repostTxt: PostModel)

data class PushRequestParams(val token: String)

data class User(val username: String)