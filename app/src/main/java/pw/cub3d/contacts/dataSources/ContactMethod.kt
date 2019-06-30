package pw.cub3d.contacts.dataSources

import android.content.Intent

data class ContactMethod(
    val icon_id: Int,
    val name: String,
    val launchIntent: (Contact)-> Intent? = {null}
)