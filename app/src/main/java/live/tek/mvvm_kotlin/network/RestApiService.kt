package live.tek.mvvm_kotlin.network

import live.tek.mvvm_kotlin.model.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {
    fun getAllPosts(onResult: (ArrayList<Post>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(IApi::class.java)
        retrofit.getPost().enqueue(object : Callback<ArrayList<Post>> {
            override fun onFailure(call: Call<ArrayList<Post>>?, t: Throwable?) {
                onResult(null)
            }

            override fun onResponse(
                call: Call<ArrayList<Post>>?,
                response: Response<ArrayList<Post>>?
            ) {
                if (response!!.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            }
        })

    }
}