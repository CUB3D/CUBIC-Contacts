package pw.cub3d.contacts

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pw.cub3d.contacts.contactslist.Contacts

class Contacts : Application() {

    private val modules = module {
        single { Contacts(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@Contacts)
            modules(modules)
        }
    }
}