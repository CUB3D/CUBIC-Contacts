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

    private val contactsList: List<Contact> by lazy {
        contactsData.second
    }

    private val contactsSparseArray: SparseArray<Contact> by lazy {
        contactsData.first
    }

    private val contactsData: Pair<SparseArray<Contact>, List<Contact>> by lazy {
        getContacts()
    }

    fun requestContacts() {
        GlobalScope.launch {
            ContactsResponse(contactsList).post()
        }
    }

    fun requestContactByID(id: Int) {
        println("Getting contact for $id")
        GlobalScope.launch {
            contactsSparseArray[id]?.let {
                ContactResponse(it).post()
            }
        }
    }

    private fun getContactProjection() = arrayOf(
        ContactsContract.Data.CONTACT_ID,
        ContactsContract.Data.RAW_CONTACT_ID,
        ContactsContract.CommonDataKinds.StructuredName.PREFIX,
        ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
        ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME,
        ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
        ContactsContract.CommonDataKinds.StructuredName.SUFFIX,
        ContactsContract.CommonDataKinds.StructuredName.PHOTO_URI,
        ContactsContract.CommonDataKinds.StructuredName.PHOTO_THUMBNAIL_URI,
        ContactsContract.CommonDataKinds.StructuredName.STARRED,
        ContactsContract.RawContacts.ACCOUNT_NAME,
        ContactsContract.RawContacts.ACCOUNT_TYPE
    )

    fun createContactsCursor(): Cursor? = ctx.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            getContactProjection(),
            "${ContactsContract.Data.MIMETYPE} = ? AND ${ContactsContract.RawContacts.ACCOUNT_TYPE} = 'at.bitfire.davdroid.address_book'",
            arrayOf(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE),
            "${ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME} COLLATE NOCASE"
        )

    fun getContacts(): Pair<SparseArray<Contact>, List<Contact>> {
        val contacts = SparseArray<Contact>()

        createContactsCursor()?.use {

            if(it.moveToFirst()) {
                do {


                    val id = it.getIntValue(ContactsContract.Data.RAW_CONTACT_ID)
                    val prefix = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.PREFIX) ?: ""
                    val firstName = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME) ?: ""
                    val middleName = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME) ?: ""
                    val surname = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME) ?: ""
                    val suffix = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.SUFFIX) ?: ""
//                    val nickname = ""
                    val photoUri = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.PHOTO_URI) ?: ""
//                    val numbers = ArrayList<PhoneNumber>()          // proper value is obtained below
//                    val emails = ArrayList<Email>()
//                    val addresses = ArrayList<Address>()
//                    val events = ArrayList<Event>()
                    val starred = it.getIntValue(ContactsContract.CommonDataKinds.StructuredName.STARRED)
                    val contactId = it.getIntValue(ContactsContract.Data.CONTACT_ID)
                    val thumbnailUri = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.PHOTO_THUMBNAIL_URI) ?: ""
//                    val notes = ""

                    var found = false

                    for(i in 0 until contacts.size()) {
                        if(contacts.valueAt(i).contactID == contactId) {
                            println("Found duplicate contact")
                            found = true
                            break
                        }
                    }

                    if(found)
                        continue

//                    val groups = ArrayList<Group>()
//                    val organization = Organization("", "")
//                    val websites = ArrayList<String>()
//                    val ims = ArrayList<IM>()
//                    val contact = Contact(id, prefix, firstName, middleName, surname, suffix, nickname, photoUri, numbers, emails, addresses,
//                        events, accountName, starred, contactID, thumbnailUri, null, notes, groups, organization, websites, ims)
//
//                    contacts.put(id, contact)

                    val contact = Contact(
                        id,
                        prefix,
                        firstName,
                        middleName,
                        surname,
                        suffix,
                        photoUri,
                        starred,
                        contactId,
                        thumbnailUri
                    )

                    phoneNumbers.getPhoneNumbers(id)[id]?.let { numbers ->
                        contact.phoneNumbers.addAll(numbers)
                    }

                    contacts.put(id, contact)

                } while (it.moveToNext())
            }
        }

        val contactsList = mutableListOf<Contact>()
        contacts.forEach { _, value ->
            contactsList.add(value)
        }

        println("Loaded ${contacts.size()} contacts")

        return contacts to contactsList
    }
}