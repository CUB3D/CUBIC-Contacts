package pw.cub3d.contacts.dataSources

data class PhoneNumber(
    val number: String,
    val type: Int,
    val label: String,
    val normalizedNumber: String
) {
    val displayLabel: String by lazy {
        if(label.isNotEmpty())
            label
        else {
            if(type == 2) {
                "Mobile"
            } else {
                "Unknown"
            }
        }
    }
}
