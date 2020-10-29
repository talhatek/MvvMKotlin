package live.tek.mvvm_kotlin.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.utils.Const
import live.tek.mvvm_kotlin.utils.Ex
import live.tek.mvvm_kotlin.utils.Resource

class RestApiService {
    @Suppress("UNCHECKED_CAST")
    fun getAllPosts(onResult: (Resource<ArrayList<Post>>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val task = withTimeoutOrNull(Const.TIME_OUT) {
                onResult(Resource.loading(data = null))
                try {
                    val res = ServiceBuilder.myApi.getPost()
                    if (res.isSuccessful) {
                        onResult(Resource.success(data = res.body()) as Resource<ArrayList<Post>>)
                    } else {
                        onResult(Resource.error(data = null, message = Ex.DATABASE_ERROR.name))
                    }

                } catch (exception: Exception) {
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