package com.ecolink.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// YourRetrofitClient.kt
object RetrofitClient {
    private const val BASE_URL = "https://ecolink.onrender.com"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

// ApiService.kt
interface ApiService {
    @FormUrlEncoded
    @POST("/api/users/signup") // Add the complete endpoint path
    suspend fun signup(
        @Field("fullname") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String,
       // @Field("dateofbirth") dateofbirth: String,
        @Field("role") role: String
    ): Response<SignupResponse>

    @FormUrlEncoded
    @POST("/api/users/signin") // Adjust the endpoint as per your API
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>
    // Add other API endpoints as needed
    @FormUrlEncoded
    @PUT("/api/users/users/{id}") // Add the complete endpoint path
    suspend fun modifyUser(
        @Path("id") userId: String,
        @Field("email") email: String?,
        @Field("profilebio") profileBio: String?,
        @Field("fullname") fullname: String?,
        @Field("location") location: String?/*,
        @Field("facebooklink") facebookLink: String?,
        @Field("instagramlink") instagramLink: String?,
        @Field("phonenumber") phoneNumber: String?,
        @Field("profilepicture") profilePicture: String?,
        @Field("dateofbirth") dateofbirth: String?,
        @Field("role") role: String?*/
    ): Response<ModifyUserResponse>
    @FormUrlEncoded
    @POST("/api/users/{id}/checkpass") // Add the complete endpoint path
    suspend fun checkPassword(
        @Path("id") userId: String,
        @Field("password") password: String
    ): Response<CheckPasswordResponse>
    @FormUrlEncoded
    @PUT("/api/users/users/{id}/deactivate") // Add the complete endpoint path
    suspend fun deactivateUser(
        @Path("id") userId: String,
        @Field("dummyField") dummyField: String = "" // Add a dummy field as @Field annotation requires at least one field
    ): Response<DeactivateUserResponse>


    @FormUrlEncoded
    @POST("/api/users/resetpassword/{id}/{newPassword}")
    suspend fun resetPassword(
        @Path("id") userId: String,
        @Path("newPassword") newPassword: String,
        @Field("dummyField") dummyField: String = ""
    ): Response<ResetPasswordResponse>
    @FormUrlEncoded
    @POST("/api/verify/generate/{id}")
    suspend fun generateVerificationToken(
        @Path("id") userId: String,
        @Field("dummyField") dummyField: String = ""
    ): Response<GenerateTokenResponse>
    @FormUrlEncoded
    @POST("/api/verify/verify/{id}/{pin}")
    suspend fun verifyUser(
        @Path("id") userId: String,
        @Path("pin") pin: String,
        @Field("dummyField") dummyField: String = ""
    ): Response<VerifyUserResponse>
}
