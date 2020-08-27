package pw.cub3d.contacts.dataSources

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.SparseArray
import androidx.core.util.forEach
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pw.cub3d.contacts.getIntValue
import pw.cub3d.contacts.getStringValue
import pw.cub3d.contacts.post

data class ContactsResponse(val contacts: List<Contact>)
data class ContactResponse(val contact: Contact)

class Contacts(private val ctx: Context, private val phoneNumbers: PhoneNumbers) {


//
    fun requestContacts() {
//        GlobalScope.launch {
//            ContactsResponse(contactsList).post()
//        }
    }
//
    fun requestContactByID(id: Int) {
//        println("Getting contact for $id")
//        GlobalScope.launch {
//            contactsSparseArray[id]?.let {
//                ContactResponse(it).post()
//            }
//        }
    }




}