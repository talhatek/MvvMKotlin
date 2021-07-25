package live.tek.mvvm_kotlin.di

import kotlinx.coroutines.Dispatchers
import live.tek.mvvm_kotlin.network.ServiceBuilder
import live.tek.mvvm_kotlin.repository.MainActivityRepository
import live.tek.mvvm_kotlin.view_model.MainActivityViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val viewModelModule = module {
    viewModel {
        MainActivityViewModel(ServiceBuilder.myApi,Dispatchers.IO)
    }
}