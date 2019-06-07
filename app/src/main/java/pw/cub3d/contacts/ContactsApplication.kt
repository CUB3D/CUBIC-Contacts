package pw.cub3d.contacts

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pw.cub3d.contacts.dataSources.ContactMethods
import pw.cub3d.contacts.dataSources.Contacts
import pw.cub3d.contacts.dataSources.PhoneNumbers

class ContactsApplication : Application() {

    private val modules = module {
        single { PhoneNumbers(get()) }
        single { Contacts(get(), get()) }
        single { ContactMethods() }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ContactsApplication)
            modules(modules)
        }
    }
}