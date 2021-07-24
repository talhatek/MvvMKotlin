package live.tek.mvvm_kotlin

import android.app.Application
import com.onesignal.OneSignal
import live.tek.mvvm_kotlin.di.mainActivityModule
import live.tek.mvvm_kotlin.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        /*OneSignal.startInit(this@MyApp)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()*/
        startKoin {
            androidLogger(level = Level.NONE)
            androidContext(androidContext = this@MyApp)
            modules(modules = listOf(viewModelModule, mainActivityModule))
        }
    }
}