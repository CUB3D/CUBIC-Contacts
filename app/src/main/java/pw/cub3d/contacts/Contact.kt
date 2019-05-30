package pw.cub3d.contacts

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
}