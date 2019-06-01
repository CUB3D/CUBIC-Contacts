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
    val contactId: Int,
    val thumbnailUri: String
) {
    val displayName: String
        get() = "$firstName $surname"

    val firstInitial: String
        get() = firstName.substring(0, 1).toUpperCase()

    val phoneticName: String
        get() = displayName

    val details: List<ContactDetail>
        get() {
            val detailsList = mutableListOf<ContactDetail>()

            detailsList.add(ContactDetail("Mobile", R.drawable.ic_phone, "010"))

            return detailsList
        }

//    val contactMethods: List<ContactMethod>
}

//inline fun <reified T> List<T>.emplce(vararg args: Any) {
//    val t = T::class.constructors.first().call(args)
//}