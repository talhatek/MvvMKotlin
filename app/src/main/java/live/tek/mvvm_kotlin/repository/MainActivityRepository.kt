package live.tek.mvvm_kotlin.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.network.RestApiService

class MainActivityRepository(val application: Application) {
    val showProgress = MutableLiveData<Boolean>()
    val postList = MutableLiveData<ArrayList<Post>>()

    fun changeState() {
        if ((showProgress.value != null && showProgress.value!!))
            showProgress.postValue(false)
        else {
            showProgress.postValue(true)

        }

    }

    fun getAllPosts() {
        changeState()
        val apiService = RestApiService()
        apiService.getAllPosts {
            if (it != null) {
                postList.postValue(it)
            }
            changeState()
        }
    }

    suspend fun getAllUsers() =
         RestApiService().getAllUsers()

}