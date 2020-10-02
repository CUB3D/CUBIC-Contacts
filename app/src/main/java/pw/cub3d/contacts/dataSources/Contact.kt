package pw.cub3d.contacts.dataSources

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.telephony.PhoneNumberUtils
import pw.cub3d.contacts.R
import java.util.*

data class Contact(
    val id: Int,
    val prefix: String,
    val firstName: String,
    val middleName: String,
    val surname: String,
    val suffix: String,
//    val nickname: String,
    val photoUri: String,
    val starred: Int,
    val contactID: Int,
    val thumbnailUri: String
) {
    val displayName: String
        get() = "$firstName $surname"

    val firstInitial: String
        get() = firstName.substring(0, 1).toUpperCase()

    val phoneticName: String
        get() = displayName

    val details: List<ContactDetail> by lazy {
            val detailsList = mutableListOf<ContactDetail>()

            phoneNumbers.forEach {
                println(it)
                detailsList.add(ContactDetail(it.displayLabel, R.drawable.ic_phone, PhoneNumberUtils.formatNumber(it.number, Locale.getDefault().country)))
            }

            detailsList
        }

    val phoneNumbers = mutableListOf<PhoneNumber>()

    val snapChatName = ""

    val preferedPhoneNumber: PhoneNumber
        get() = phoneNumbers.first()

    val emailAddresses = mutableListOf<String>()

    val hasPhoto = photoUri.isNotBlank()

    fun getImageBitmap(ctx: Context): Bitmap? = MediaStore.Images.Media.getBitmap(ctx.contentResolver, Uri.parse(photoUri))

    fun contains(searchQuery: String) =
        listOf(
            prefix,
            firstName,
            surname,
            middleName,
            suffix,
        ).filter {
            it.isNotBlank()
        }.map {
            it.toLowerCase(Locale.getDefault())
        }.any {
            searchQuery.contains(it) || it.contains(searchQuery)
        }

//    val contactMethods: List<ContactMethod>
}