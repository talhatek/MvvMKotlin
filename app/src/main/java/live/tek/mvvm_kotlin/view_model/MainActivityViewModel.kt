package live.tek.mvvm_kotlin.view_model

import android.app.Application
import android.widget.Toast
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
    val postList: LiveData<ArrayList<Post>>
    private val app=application
    init {
        this.showProgress = repository.showProgress
        this.postList = repository.postList
      //  getAllPosts()
    }

    fun changeState() {
        repository.changeState()
    }
    fun doToast(){
        Toast.makeText(app.applicationContext,"fdf",Toast.LENGTH_SHORT).show()
    }

    private fun getAllPosts() {
        repository.getAllPosts()
    }

    fun getUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getAllUsers()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }

    }


}