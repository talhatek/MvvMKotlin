package live.tek.mvvm_kotlin.repository

import androidx.lifecycle.MutableLiveData
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.network.IApi
import live.tek.mvvm_kotlin.utils.Resource

class MainActivityRepository(private val networkSource: IApi) {
    val showProgress = MutableLiveData<Boolean>()
    val postList = MutableLiveData<Resource<List<Post>>>()

    fun changeState() {
        if ((showProgress.value != null && showProgress.value!!))
            showProgress.postValue(false)
        else {
            showProgress.postValue(true)

        }

    }

   suspend fun getAllPosts() {
        val data = networkSource.getPost()

        if(data.code()==200){
            postList.postValue(Resource.success(data = data.body() as List<Post>))

        }
        else{
            postList.postValue(Resource.error(data = null,message = "error"))
        }


    }

     suspend fun getAllUsers() =
        networkSource.getUsers()


}