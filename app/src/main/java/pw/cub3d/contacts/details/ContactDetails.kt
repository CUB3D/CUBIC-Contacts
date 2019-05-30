package pw.cub3d.contacts.details


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_contact_details.*

import pw.cub3d.contacts.R
import pw.cub3d.contacts.contactslist.Contact

class ContactDetails : Fragment() {

    var contact: Contact? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contact_avatarIcon.setText(contact!!.firstInitial)
        contact_name.text = contact!!.displayName
        contact_phonetic_name.text = contact!!.phoneticName
    }

    companion object {
        fun createInstance(contact: Contact): ContactDetails {
            val instance = ContactDetails()
            instance.contact = contact
            return instance
        }
    }
}
