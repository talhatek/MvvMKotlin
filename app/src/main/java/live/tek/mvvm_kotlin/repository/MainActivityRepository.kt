package live.tek.mvvm_kotlin.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.network.RestApiService
import live.tek.mvvm_kotlin.utils.Resource

class MainActivityRepository() {
    val showProgress = MutableLiveData<Boolean>()
    val postList = MutableLiveData<Resource<List<Post>>>()

    fun changeState() {
        if ((showProgress.value != null && showProgress.value!!))
            showProgress.postValue(false)
        else {
            showProgress.postValue(true)

        }

    }

    fun getAllPosts() {
        val apiService = RestApiService()
        apiService.getAllPosts {
            if (it != null) {
                postList.postValue(it)
            }
        }
    }

    suspend fun getAllUsers() =
         RestApiService().getAllUsers()

}