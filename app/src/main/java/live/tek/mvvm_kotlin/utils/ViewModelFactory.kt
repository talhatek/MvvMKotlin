package live.tek.mvvm_kotlin.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import live.tek.mvvm_kotlin.network.ServiceBuilder
import live.tek.mvvm_kotlin.repository.MainActivityRepository
import live.tek.mvvm_kotlin.view_model.MainActivityViewModel
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val application: Application) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom((MainActivityViewModel::class.java))){
            return MainActivityViewModel(MainActivityRepository(ServiceBuilder.myApi)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}