package pw.cub3d.contacts.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import pw.cub3d.contacts.R

import kotlinx.android.synthetic.main.activity_contact_details.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import pw.cub3d.contacts.dataSources.ContactMethod
import pw.cub3d.contacts.dataSources.ContactMethods
import pw.cub3d.contacts.dataSources.ContactResponse
import pw.cub3d.contacts.dataSources.Contacts
import pw.cub3d.contacts.register
import pw.cub3d.contacts.unregister

class ContactDetailsActivity : AppCompatActivity() {

    val contacts: Contacts by inject()
    val contactMethods: ContactMethods by inject()

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

        contact_contactMethodsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        contact_contactMethodsRecycler.adapter = ContactMethodsAdapter(this, contactMethods.ALL_CONTACT_METHODS.toList())

        contact_contactDetails.layoutManager = LinearLayoutManager(this)
        contact_contactDetails.adapter = ContactDetailsAdapter(this, contact.contact.details)
    }

    companion object {
        const val CONTACT_ID = "CONTACT_ID"
    }
}
