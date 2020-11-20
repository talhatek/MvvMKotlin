package live.tek.mvvm_kotlin.di

import live.tek.mvvm_kotlin.view_model.MainActivityViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainActivityViewModel(get())
    }
}