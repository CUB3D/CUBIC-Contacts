package pw.cub3d.contacts

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pw.cub3d.contacts.dataSources.*

class ContactsApplication : Application() {

    private val modules = module {
        single { PhoneNumbers(get()) }
//        single<IContactRepository> { NullContactRepository() }
        single<IContactRepository> { ContactRepository(get(), get()) }
        single { ContactMethods() }
        single { Contacts(get(), get())}
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