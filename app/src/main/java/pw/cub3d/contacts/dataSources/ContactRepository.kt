package pw.cub3d.contacts.dataSources

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.SparseArray
import androidx.compose.runtime.emit
import androidx.core.util.valueIterator
import kotlinx.coroutines.flow.*
import pw.cub3d.contacts.getIntValue
import pw.cub3d.contacts.getStringValue

interface IContactRepository {
    fun getContacts(): Flow<List<Contact>>
    fun getContactById(id: Int): Flow<Contact?>
}

class NullContactRepository : IContactRepository {
    override fun getContacts(): Flow<List<Contact>> = flow {
        emit(emptyList())
    }

    override fun getContactById(id: Int): Flow<Contact?> = flow {
        emit(null)
    }
}

class ContactRepository(private val ctx: Context, private val phoneNumbers: PhoneNumbers): IContactRepository {
    override fun getContacts(): Flow<List<Contact>> =
        readContactDb().map { it.valueIterator().asSequence().toList().sortedBy { it.firstInitial } }

    override fun getContactById(id: Int): Flow<Contact?> =
        readContactDb().map { it.get(id) }



    fun createContactsCursor(): Cursor? = ctx.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            getContactProjection(),
            "${ContactsContract.Data.MIMETYPE} = ? AND ${ContactsContract.RawContacts.ACCOUNT_TYPE} = 'at.bitfire.davdroid.address_book'",
            arrayOf(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE),
            "${ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME} COLLATE NOCASE"
    )

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

    fun readContactDb() = flow {
        val contacts = SparseArray<Contact>()

        createContactsCursor()?.use {

            if (it.moveToFirst()) {
                do {


                    val id = it.getIntValue(ContactsContract.Data.RAW_CONTACT_ID)
                    val prefix = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.PREFIX)
                            ?: ""
                    val firstName = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME)
                            ?: ""
                    val middleName = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME)
                            ?: ""
                    val surname = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME)
                            ?: ""
                    val suffix = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.SUFFIX)
                            ?: ""
//                    val nickname = ""
                    val photoUri = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.PHOTO_URI)
                            ?: ""
//                    val numbers = ArrayList<PhoneNumber>()          // proper value is obtained below
//                    val emails = ArrayList<Email>()
//                    val addresses = ArrayList<Address>()
//                    val events = ArrayList<Event>()
                    val starred = it.getIntValue(ContactsContract.CommonDataKinds.StructuredName.STARRED)
                    val contactId = it.getIntValue(ContactsContract.Data.CONTACT_ID)
                    val thumbnailUri = it.getStringValue(ContactsContract.CommonDataKinds.StructuredName.PHOTO_THUMBNAIL_URI)
                            ?: ""
//                    val notes = ""

                    var found = false

                    for (i in 0 until contacts.size()) {
                        if (contacts.valueAt(i).contactID == contactId) {
                            found = true
                            break
                        }
                    }

                    if (found)
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

        emit(contacts)
    }
}