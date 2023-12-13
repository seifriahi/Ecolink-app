package com.ecolink.api
import com.ecolink.models.Lesson
import com.ecolink.models.Comment
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("comments/{lessonId}")
    suspend fun getCommentsByLessonId(@Path("lessonId") lessonId: String): Response<List<Comment>>

    @POST("comments")
    suspend fun addComment(@Body comment: Comment): Response<Comment>

    @DELETE("lessons/{lessonId}")
    suspend fun deleteLesson(@Path("lessonId") lessonId: String): Response<Void>

    @GET("/allLesson")
    fun allproject(): Call<List<Lesson>>
    @POST("addlesson")
    @Multipart
    fun addProject(
        @Part image: MultipartBody.Part,
        @Part("title") title: String,
        @Part("description") description: String
    ): Call<Lesson>

    companion object {
        private const val BASE_URL = "https://ecolink.onrender.com"

        fun create(): Api {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(Api::class.java)
        }
    }
}
