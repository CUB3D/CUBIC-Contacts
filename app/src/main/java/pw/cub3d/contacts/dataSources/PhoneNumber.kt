package pw.cub3d.contacts.dataSources

import android.net.Uri

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

    // Returns a number dialable via a tel:0000 intent
    val dialableNumber: String
     get() =
         if(number.contains("#")) {
             number.replace("#", Uri.encode("#"))
         } else {
             number
         }
}
