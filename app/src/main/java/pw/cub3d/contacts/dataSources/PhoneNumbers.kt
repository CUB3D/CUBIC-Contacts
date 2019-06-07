package pw.cub3d.contacts.dataSources

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.SparseArray
import pw.cub3d.contacts.getIntValue
import pw.cub3d.contacts.getStringValue

class PhoneNumbers(val context: Context) {

//    fun Context.getAllContactSources(): List<ContactSource> {
//        val sources = ContactsHelper(this).getDeviceContactSources()
//        sources.add(getPrivateContactSource())
//        return sources.toMutableList()
//    }
//
//    fun Context.getVisibleContactSources(): ArrayList<String> {
//        val sources = getAllContactSources()
//        val ignoredContactSources = config.ignoredContactSources
//        return ArrayList(sources).filter { !ignoredContactSources.contains(it.getFullIdentifier()) }
//            .map { it.name }.toMutableList() as ArrayList<String>
//    }
//
//    val displayContactSources: List<String>
//
//
//    get() {
//        return context.contacgetVisibleContactSources()
//    }
//
//    private fun getSourcesSelection(addMimeType: Boolean = false, addContactId: Boolean = false, useRawContactId: Boolean = true): String {
//        val strings = ArrayList<String>()
//        if (addMimeType) {
//            strings.add("${ContactsContract.Data.MIMETYPE} = ?")
//        }
//
//        if (addContactId) {
//            strings.add("${if (useRawContactId) ContactsContract.Data.RAW_CONTACT_ID else ContactsContract.Data.CONTACT_ID} = ?")
//        } else {
//            // sometimes local device storage has null account_name, handle it properly
//            val accountnameString = StringBuilder()
//            if (displayContactSources.contains("")) {
//                accountnameString.append("(")
//            }
//            accountnameString.append("${ContactsContract.RawContacts.ACCOUNT_NAME} IN (${getQuestionMarks()})")
//            if (displayContactSources.contains("")) {
//                accountnameString.append(" OR ${ContactsContract.RawContacts.ACCOUNT_NAME} IS NULL)")
//            }
//            strings.add(accountnameString.toString())
//        }
//
//        return TextUtils.join(" AND ", strings)
//    }
//
//    private fun getSourcesSelectionArgs(mimetype: String? = null, contactId: Int? = null): Array<String> {
//        val args = ArrayList<String>()
//
//        if (mimetype != null) {
//            args.add(mimetype)
//        }
//
//        if (contactId != null) {
//            args.add(contactId.toString())
//        } else {
//            args.addAll(displayContactSources.filter { it.isNotEmpty() })
//        }
//
//        return args.toTypedArray()
//    }

    fun getPhoneNumbers(contactId: Int? = null): SparseArray<ArrayList<PhoneNumber>> {
        val phoneNumbers = SparseArray<ArrayList<PhoneNumber>>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.Data.RAW_CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.LABEL
        )

//        val selection = if (contactId == null) getSourcesSelection() else "${ContactsContract.Data.RAW_CONTACT_ID} = ?"
//        val selectionArgs = if (contactId == null) getSourcesSelectionArgs() else arrayOf(contactId.toString())

        val selection = "${ContactsContract.Data.RAW_CONTACT_ID} = ?"
        val selectionArgs = arrayOf(contactId.toString())

        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor?.moveToFirst()!!) {
                do {
                    val id = cursor.getIntValue(ContactsContract.Data.RAW_CONTACT_ID)
                    val number = cursor.getStringValue(ContactsContract.CommonDataKinds.Phone.NUMBER) ?: continue
                    val normalizedNumber = cursor.getStringValue(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER) ?: "TODO: fix normalize in phonenumbers"//number.normalizeNumber()
                    val type = cursor.getIntValue(ContactsContract.CommonDataKinds.Phone.TYPE)
                    val label = cursor.getStringValue(ContactsContract.CommonDataKinds.Phone.LABEL) ?: ""

                    if (phoneNumbers[id] == null) {
                        phoneNumbers.put(id, ArrayList())
                    }

                    val phoneNumber = PhoneNumber(number, type, label, normalizedNumber)
                    phoneNumbers[id].add(phoneNumber)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
//            context.showErrorToast(e)
            TODO("Error handling")
        } finally {
            cursor?.close()
        }

        return phoneNumbers
    }
}