package ru.tutu.tutuemployee

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.tutu.tutuemployee.di.appModules

class TutuEmployeeApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TutuEmployeeApp)
            modules(appModules)
        }
    }
}
