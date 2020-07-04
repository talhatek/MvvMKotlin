package live.tek.mvvm_kotlin.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.repository.MainActivityRepository

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MainActivityRepository(application)
    val showProgress: LiveData<Boolean>
    val postList: LiveData<ArrayList<Post>>

    init {
        this.showProgress = repository.showProgress
        this.postList = repository.postList

    }

    fun changeState() {
        repository.changeState()
    }

    fun getAllPosts() {
        repository.getAllPosts()
    }


}