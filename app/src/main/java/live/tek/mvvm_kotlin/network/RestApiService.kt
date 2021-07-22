package live.tek.mvvm_kotlin.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.utils.Const
import live.tek.mvvm_kotlin.utils.Ex
import live.tek.mvvm_kotlin.utils.Resource
import retrofit2.HttpException

class RestApiService {
    @Suppress("UNCHECKED_CAST")
    fun getAllPosts(onResult: (Resource<ArrayList<Post>>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val task = withTimeoutOrNull(Const.TIME_OUT) {
                onResult(Resource.loading(data = null))
                try {
                    val res = ServiceBuilder.myApi.getPost()
                    if (res.code()==200) {
                        onResult(Resource.success(data = res.body()) as Resource<ArrayList<Post>>)
                    } else if(res.code()==201)/*change error type*/ {
                        onResult(Resource.error(data = null, message = Ex.DATABASE_ERROR.name))
                    }

                } catch (exception: Exception) {
                    if (exception is HttpException) {

                        Log.e("httpEx", exception.code().toString())
                    } else {
                        Log.e("httpEx", "nope")

                    }

                    onResult(Resource.error(data = null, message = Ex.NO_CONNECTION.name))
                }
            }
            if (task == null) {
                onResult(Resource.error(data = null, message = Ex.TIME_OUT.name))
            }

        }


    }

    suspend fun getAllUsers() =
        ServiceBuilder.myApi.getUsers()
}