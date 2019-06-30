package pw.cub3d.contacts.contactslist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pw.cub3d.contacts.R
import pw.cub3d.contacts.dataSources.Contact
import pw.cub3d.contacts.post

class ContactsAdapter(ctx: Context, private val contacts: List<Contact>): RecyclerView.Adapter<ContactViewHolder>() {
    private val inflater = LayoutInflater.from(ctx)!!


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(inflater.inflate(R.layout.contact_list_entry, parent, false))
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        val contactBefore = contacts.getOrNull(position - 1)

        holder.name.text = contact.displayName
        holder.icon.setText(contact.firstInitial)

        holder.header.text = contact.firstInitial

        contactBefore?.let {
            if(it.firstInitial == contact.firstInitial) {
                holder.header.visibility = View.INVISIBLE
            } else {
                holder.header.visibility = View.VISIBLE
            }
        }

        holder.root.setOnClickListener {
            ContactSelectedEvent(contact).post()
        }
    }

}

data class ContactSelectedEvent(val contact: Contact)