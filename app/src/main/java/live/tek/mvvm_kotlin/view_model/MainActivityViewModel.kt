package live.tek.mvvm_kotlin.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.model.User
import live.tek.mvvm_kotlin.repository.MainActivityRepository
import live.tek.mvvm_kotlin.utils.Resource
import java.util.*

class MainActivityViewModel(private val repository: MainActivityRepository) : ViewModel() {
    @ExperimentalCoroutinesApi
    internal val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)
    var userList = listOf<User>()
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



    private fun dataFromNetwork(query: String):Resource<List<User>> {

        userList.filter {
            it.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))
            // tmp.add(it)
        }.let {
            return Resource.success(data = it)

        }

    }
    @FlowPreview
    @ExperimentalCoroutinesApi
    val searchLiveData =searchResult.asLiveData()


    val showProgress: LiveData<Boolean>
    val postList: LiveData<Resource<List<Post>>>

    init {

        this.showProgress = repository.showProgress
        this.postList = repository.postList

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