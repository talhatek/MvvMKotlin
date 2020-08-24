package live.tek.mvvm_kotlin.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.utils.Resource

class RestApiService {
    @Suppress("UNCHECKED_CAST")
    fun getAllPosts(onResult: (Resource<ArrayList<Post>>) -> Unit) {
        val retrofit = ServiceBuilder.buildService(IApi::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            onResult(Resource.loading(data = null))
            try {
                val res = retrofit.getPost()
                if (res.isSuccessful) {
                    onResult(Resource.success(data = res.body()) as Resource<ArrayList<Post>>)
                } else {
                    Log.e("testing", res.code().toString())
                    onResult(Resource.error(data = null, message = res.code().toString()))
                }

            } catch (exception: Exception) {
                Log.e("testing", exception.message!!)
                onResult(Resource.error(data = null, message = exception.message.toString()))
            }
        }


    }

    suspend fun getAllUsers() =
        ServiceBuilder.buildService(IApi::class.java).getUsers()

}