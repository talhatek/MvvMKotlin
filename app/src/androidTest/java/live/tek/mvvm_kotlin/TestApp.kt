package live.tek.mvvm_kotlin

import android.app.Application
import live.tek.mvvm_kotlin.di.mainActivityModule
import live.tek.mvvm_kotlin.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level

class TestApp:Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.NONE)
            androidContext(this@TestApp)
            modules(modules = listOf(viewModelModule, mainActivityModule))

        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}