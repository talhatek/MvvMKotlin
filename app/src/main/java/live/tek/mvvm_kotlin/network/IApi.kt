package live.tek.mvvm_kotlin.network

import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import kotlin.coroutines.CoroutineContext

interface IApi {
    @GET("posts")
  /* suspend i have to remove suspend for testing todo */ fun getPost(): Response<ArrayList<Post>>

    @GET("users")
   suspend fun getUsers():List<User>
}
