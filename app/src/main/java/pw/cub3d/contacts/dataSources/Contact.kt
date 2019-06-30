package pw.cub3d.contacts.dataSources

import pw.cub3d.contacts.R

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
                detailsList.add(ContactDetail(it.displayLabel, R.drawable.ic_phone, it.normalizedNumber))
            }

            detailsList
        }

    val phoneNumbers = mutableListOf<PhoneNumber>()

    val snapChatName = ""

    val preferedPhoneNumber: PhoneNumber
        get() = phoneNumbers.first()

    val emailAddresses = mutableListOf<String>()

//    val contactMethods: List<ContactMethod>
}