package live.tek.mvvm_kotlin.di

import live.tek.mvvm_kotlin.adapter.PostAdapter
import live.tek.mvvm_kotlin.adapter.UserAdapter
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.model.User
import org.koin.dsl.module
import java.util.*


class MainActivityDI(
    val postAdapter: PostAdapter,
    val userAdapter: UserAdapter,
    var aryPost: ArrayList<Post>,
    var aryUser: ArrayList<User>
)

val mainActivityModule = module {
    factory {
        MainActivityDI(
            PostAdapter(arrayListOf()), UserAdapter(arrayListOf()),
            arrayListOf(), arrayListOf()
        )
    }
}
