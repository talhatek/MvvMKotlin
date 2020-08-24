package live.tek.mvvm_kotlin.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import live.tek.mvvm_kotlin.model.Post

class RestApiService {
    fun getAllPosts(onResult: (ArrayList<Post>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(IApi::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val res = retrofit.getPost()
                if (res.isSuccessful) {
                    onResult(res.body())
                } else {
                    Log.e("testing", res.code().toString())
                    onResult(null)
                }

            } catch (exception: Exception) {
                Log.e("testing", exception.message!!)
                onResult(null)
            }
        }


    }

    suspend fun getAllUsers() =
        ServiceBuilder.buildService(IApi::class.java).getUsers()

}