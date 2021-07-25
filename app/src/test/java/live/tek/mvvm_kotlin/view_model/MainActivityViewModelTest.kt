package live.tek.mvvm_kotlin.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import live.tek.mvvm_kotlin.MainCoroutineRule
import live.tek.mvvm_kotlin.getOrAwaitValueTest
import live.tek.mvvm_kotlin.mock
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.network.IApi
import live.tek.mvvm_kotlin.repository.MainActivityRepository
import live.tek.mvvm_kotlin.utils.Status
import live.tek.mvvm_kotlin.whenever
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.net.ConnectException

class MainActivityViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    private lateinit var mainActivityViewModel: MainActivityViewModel

    private var myApi = mock<IApi>()


    @Before
    fun setUp() {
        mainActivityViewModel = MainActivityViewModel(MainActivityRepository(myApi))

    }


    @Test
    fun `test with mockito ,success`()  {
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
        assert(mainActivityViewModel.postList.getOrAwaitValueTest().status == Status.SUCCESS)


    }

    @Test
    fun `test with mockito ,failure`() {
        val errorResponse =
            "{\n" +
                    "  \"type\": \"error\",\n" +
                    "  \"message\": \"What you were looking for isn't here.\"\n" + "}"
        whenever(myApi.getPost()).thenReturn(
            Response.error(
                500, errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
            )
        )
        mainActivityViewModel.getAllPosts()
        assert(mainActivityViewModel.postList.getOrAwaitValueTest().status == Status.ERROR)


    }
}