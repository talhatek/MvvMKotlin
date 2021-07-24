package live.tek.mvvm_kotlin.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.jakewharton.espresso.OkHttp3IdlingResource
import live.tek.mvvm_kotlin.*
import live.tek.mvvm_kotlin.network.ApiUrl
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        ApiUrl.url = "http://127.0.0.1:8080/"
        mockWebServer.start(8080)
        IdlingRegistry.getInstance()
            .register(OkHttp3IdlingResource.create("okHttp", OkHttpClient()))


    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun succeed_getUsers_case() {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200)
                .setBody(FileReader.readStringFromFile("success_users.json"))
        )

        onView(withId(R.id.rv)).isVisible()


    }

    @Test
    fun failed_getUsers_case() {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(500)
        )

        onView(withId(R.id.rv)).isInvisible()
    }

    @Test
    fun searchView_successful_search_case() {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200)
                .setBody(FileReader.readStringFromFile("success_users.json"))
        )
        onView(withId(R.id.et_search)).perform(
            click()
        ).perform(typeSearchViewText("leanne"))
        Thread.sleep(1000)
        onView(withId(R.id.rv)).check(matches(hasChildCount(1)))

    }
}