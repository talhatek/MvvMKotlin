package live.tek.mvvm_kotlin.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.network.RestApiService

class MainActivityRepository(val application: Application) {
    val showProgress = MutableLiveData<Boolean>()
    val postList = MutableLiveData<ArrayList<Post>>()

    fun changeState() {
        showProgress.value = !(showProgress.value != null && showProgress.value!!)

    }

    fun getAllPosts() {
        changeState()
        val apiService = RestApiService()

        apiService.getAllPosts {
            if (it != null) {
                postList.value = it
            }
            changeState()
        }
    }
}