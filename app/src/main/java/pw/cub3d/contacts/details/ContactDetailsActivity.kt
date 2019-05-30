package pw.cub3d.contacts.details

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import pw.cub3d.contacts.R

import kotlinx.android.synthetic.main.activity_contact_details.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import pw.cub3d.contacts.contactslist.Contact
import pw.cub3d.contacts.contactslist.ContactResponse
import pw.cub3d.contacts.contactslist.Contacts
import pw.cub3d.contacts.register
import pw.cub3d.contacts.unregister

class ContactDetailsActivity : AppCompatActivity() {

    val contacts: Contacts by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

        register()

        intent.getIntExtra(CONTACT_ID, -1).takeIf {
            it > 0
        }?.let {
            contacts.requestContactByID(it)
        }
    }

    override fun onDestroy() {
        unregister()
        super.onDestroy()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onContactCallback(contact: ContactResponse) {
        val con = contact.contact

        contact_avatarIcon.setText(con.firstInitial)
        contact_name.text = con.displayName
        contact_phonetic_name.text = con.phoneticName
    }

    companion object {
        const val CONTACT_ID = "CONTACT_ID"
    }
}
