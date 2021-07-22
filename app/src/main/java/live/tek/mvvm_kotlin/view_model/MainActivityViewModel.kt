package live.tek.mvvm_kotlin.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.repository.MainActivityRepository
import live.tek.mvvm_kotlin.utils.Resource
import androidx.lifecycle.asLiveData
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    @ExperimentalCoroutinesApi
    internal val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    @ExperimentalCoroutinesApi
    @FlowPreview
    val searchResult =
        queryChannel.asFlow().debounce(555L).filter { q -> return@filter q.isNotEmpty() }
            .distinctUntilChanged()
            .mapLatest {q->
                try {
                dataFromNetwork(q)

            }
            catch (ex:Exception)
            {
                Resource.error(data = null,"errorr")
            }
            }.flowOn(Dispatchers.IO)
            .catch { emit(Resource.error(data = null,it.message ?: "error occurred")) }



    private fun dataFromNetwork(query: String):Resource<List<String>> {
        Log.e("searchDataCame","ji")
        val myList = listOf("talha", "tek", "yusuf", "taha", "tuncer")

        val tmp = arrayListOf<String>()
        myList.forEach {
            if (it.contains(query))
               throw Exception("flow Ex")
               // tmp.add(it)
        }
        return Resource.success(data = tmp)

    }
    @FlowPreview
    @ExperimentalCoroutinesApi
    val searchLiveData =searchResult.asLiveData()

    private val repository = MainActivityRepository()
    val showProgress: LiveData<Boolean>
    val postList: LiveData<Resource<List<Post>>>

    init {

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
        Log.e("viewModel", "getPostsEmitted")
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