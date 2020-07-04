package live.tek.mvvm_kotlin.network

import live.tek.mvvm_kotlin.model.Post
import retrofit2.Call
import retrofit2.http.GET

interface IApi {
    @GET("posts")
    fun getPost(): Call<ArrayList<Post>>

}