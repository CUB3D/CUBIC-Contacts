package pw.cub3d.contacts.contactslist

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.SparseArray
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pw.cub3d.contacts.getIntValue
import pw.cub3d.contacts.getStringValue
import pw.cub3d.contacts.post

data class ContactsResponse(val contacts: SparseArray<Contact>)

class Contacts(private val ctx: Context) {

    fun requestContacts() {
        GlobalScope.launch {
            ContactsResponse(getContacts()).post()
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
            "${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE),
            "${ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME} COLLATE NOCASE"
        )

    fun parseContact() {

    }

    fun getContacts(): SparseArray<Contact> {
        val contacts = SparseArray<Contact>()

        createContactsCursor()?.use {

            if(it.moveToFirst()) {
                do {
                    val accountName =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME))
                            ?: ""
                    println("Acc name: $accountName")
                    val accountType = it.getStringValue(ContactsContract.RawContacts.ACCOUNT_TYPE) ?: ""
//                    if (ignoredSources.contains("$accountName:$accountType")) {
//                        continue
//                    }

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



//                    val groups = ArrayList<Group>()
//                    val organization = Organization("", "")
//                    val websites = ArrayList<String>()
//                    val ims = ArrayList<IM>()
//                    val contact = Contact(id, prefix, firstName, middleName, surname, suffix, nickname, photoUri, numbers, emails, addresses,
//                        events, accountName, starred, contactId, thumbnailUri, null, notes, groups, organization, websites, ims)
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

                    contacts.put(id, contact)

                } while (it.moveToNext())
            }
        }

        return contacts
    }
}