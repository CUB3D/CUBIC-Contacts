package pw.cub3d.contacts.dataSources

import android.content.Intent
import android.net.Uri
import pw.cub3d.contacts.R


class ContactMethods {
    val ALL_CONTACT_METHODS = arrayOf(
        ContactMethod(R.drawable.ic_phone, "Call") {
            Intent(Intent.ACTION_CALL, Uri.parse("tel:${it.preferedPhoneNumber}"))
        },
        ContactMethod(R.drawable.ic_message_square, "Text") {
            val sendIntent = Intent(Intent.ACTION_VIEW)
            sendIntent.data = Uri.parse("sms:${it.preferedPhoneNumber}")
            sendIntent
        },
        ContactMethod(R.drawable.ic_mail, "Email") {
            if(it.emailAddresses.isEmpty()) {
                null
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("mailto:${it.emailAddresses.first()}")

                intent
            }
        },
        ContactMethod(R.drawable.ic_snapchat, "Snapchat") {
            if(it.snapChatName.isEmpty()) {
                 null
            } else {
                val snapURL = Uri.parse("https://snapchat.com/add/${it.snapChatName}")

                try {
                    val intent = Intent(Intent.ACTION_VIEW, snapURL)
                    intent.setPackage("com.snapchat.android")
                    intent
                } catch (e: Exception) {
                    Intent(Intent.ACTION_VIEW, snapURL)
                }
            }
        }
    )
}