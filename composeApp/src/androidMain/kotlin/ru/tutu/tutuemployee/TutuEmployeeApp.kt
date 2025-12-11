package ru.tutu.tutuemployee

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.tutu.tutuemployee.di.appModules
import ru.tutu.tutuemployee.presentation.auth.AndroidContextProvider

class TutuEmployeeApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Сохраняем контекст для использования в expect/actual функциях
        AndroidContextProvider.applicationContext = this

        startKoin {
            androidLogger()
            androidContext(this@TutuEmployeeApp)
            modules(appModules)
        }
    }
}
