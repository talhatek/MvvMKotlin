package live.tek.mvvm_kotlin.view_model

import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.model.SimpleError
import live.tek.mvvm_kotlin.model.User
import live.tek.mvvm_kotlin.network.IApi
import live.tek.mvvm_kotlin.utils.Resource
import java.util.*

class MainActivityViewModel(private val myApi: IApi,private val scope:CoroutineDispatcher) : ViewModel() {
    @ExperimentalCoroutinesApi
    internal val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)
    var userList = listOf<User>()
  private  val postList = MutableLiveData<Resource<List<Post>>>()
     val postListLiveData: LiveData<Resource<List<Post>>>
        get() = postList

    @ExperimentalCoroutinesApi
    @FlowPreview
    val searchResult =
        queryChannel.asFlow().debounce(555L).filter { q -> return@filter q.isNotEmpty() }
            .distinctUntilChanged()
            .mapLatest { q ->
                try {
                    dataFromNetwork(q)

                } catch (ex: Exception) {
                    Resource.error(data = null, "errorr")
                }
            }.flowOn(Dispatchers.IO)
            .catch { emit(Resource.error(data = null, it.message ?: "error occurred")) }


    private fun dataFromNetwork(query: String): Resource<List<User>> {

        userList.filter {
            it.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))
        }.let {
            return Resource.success(data = it)

        }

    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    val searchLiveData = searchResult.asLiveData()

    init {

//        this.showProgress = repository.showProgress
//        this.postList = repository.postList

    }


    fun getAllPosts() {
//        Log.e("viewModel", "getPostsEmitted")
        viewModelScope.launch(scope) {
            val a = myApi.getPost()
            if(a.code()==200){
                postList.postValue(Resource.success(data = a.body() as List<Post>))

            }
            else if(a.code()==500){
                val gson = Gson()
                val type = object : TypeToken<SimpleError>() {}.type
                val errorResponse: SimpleError? = gson.fromJson(a.errorBody()!!.charStream(), type)
                postList.postValue(Resource.error(data = null,errorResponse?.message ?:"null"))
            }
        }
    }

    fun getUsers() = liveData(Dispatchers.IO) {
        Log.e("viewModel", "getUsersEmitted")
        emit(Resource.loading(data = null))
        try {
            val tmp = myApi.getUsers()
            userList = tmp
            emit(Resource.success(data = tmp))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }

    }

    override fun onCleared() {
        super.onCleared()
        Log.e("cleared", "MainActivityViewModel gone")

    }

}