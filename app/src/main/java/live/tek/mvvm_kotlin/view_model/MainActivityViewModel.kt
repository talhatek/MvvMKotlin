package live.tek.mvvm_kotlin.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.repository.MainActivityRepository
import live.tek.mvvm_kotlin.utils.Resource

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MainActivityRepository(application)
    val showProgress: LiveData<Boolean>
    val postList: LiveData<Resource<List<Post>>>

    init {
        //getAllPosts()
        this.showProgress = repository.showProgress
        this.postList = repository.postList
    }

    fun changeState() {
        repository.changeState()
    }

    fun doToast() {
       // Toast.makeText(getApplication(), "Test", Toast.LENGTH_SHORT).show()
    }

    fun getAllPosts() {
        Log.e("viewModel","getPostsEmitted")
        repository.getAllPosts()
    }

    fun getUsers() = liveData(Dispatchers.IO) {
        Log.e("viewModel", "getUsersEmitted")
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getAllUsers()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }

    }

    override fun onCleared() {
        super.onCleared()
        Log.e("cleared", "MainActivityViewModel gone")

    }

}