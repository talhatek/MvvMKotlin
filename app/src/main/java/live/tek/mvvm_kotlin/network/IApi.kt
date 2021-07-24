package live.tek.mvvm_kotlin.network

import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.model.User
import retrofit2.Response
import retrofit2.http.GET

interface IApi {
    @GET("posts")
    suspend fun getPost(): Response<ArrayList<Post>>

    @GET("users")
    suspend  fun getUsers():List<User>
}