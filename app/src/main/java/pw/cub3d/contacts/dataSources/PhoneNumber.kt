package pw.cub3d.contacts.dataSources

data class PhoneNumber(
    val number: String,
    val type: Int,
    val label: String,
    val normalizedNumber: String
)
