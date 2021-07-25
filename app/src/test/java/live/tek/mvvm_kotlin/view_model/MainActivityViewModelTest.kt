package live.tek.mvvm_kotlin.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import live.tek.mvvm_kotlin.getOrAwaitValueTest
import live.tek.mvvm_kotlin.mock
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.model.SimpleError
import live.tek.mvvm_kotlin.network.IApi
import live.tek.mvvm_kotlin.network.ServiceBuilder
import live.tek.mvvm_kotlin.utils.Status
import live.tek.mvvm_kotlin.whenever
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.*
import retrofit2.Response.error

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {
    //
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

//    @ExperimentalCoroutinesApi
//    @get:Rule
//    val mainCoroutineRule = MainCoroutineRule()
//    @get:Rule
//    val testCoroutineRule = TestCoroutineRule()

    private lateinit var mainActivityViewModel: MainActivityViewModel

    private var myApi = mock<IApi>()

    private val testDispatcher = TestCoroutineDispatcher()


    @Before
    fun setUp() {
        mainActivityViewModel = MainActivityViewModel(myApi, testDispatcher)

    }

    @Test
    fun `test postListLiveData, result success`() = runBlockingTest {
        whenever(myApi.getPost()).thenReturn(
            Response.success(
                arrayListOf(
                    Post(
                        "body",
                        1,
                        "title",
                        11
                    )
                )
            )
        )
        mainActivityViewModel.getAllPosts()
        assert(mainActivityViewModel.postListLiveData.getOrAwaitValueTest().status == Status.SUCCESS)
    }



    @Test
    fun `test with mockito ,failure`() = runBlockingTest {
        val errorResponse =Gson().toJson(SimpleError(500,"server error"))
         whenever(myApi.getPost()).thenReturn(
            error(
                500, errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
            )
        )
        mainActivityViewModel.getAllPosts()
        assert(mainActivityViewModel.postListLiveData.getOrAwaitValueTest().message == "server error")


    }
}