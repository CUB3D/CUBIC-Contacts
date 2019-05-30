package pw.cub3d.contacts.contactslist

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.size
import androidx.recyclerview.widget.RecyclerView
import pw.cub3d.contacts.Contact
import pw.cub3d.contacts.R

class ContactsAdapter(ctx: Context, private val contacts: SparseArray<Contact>): RecyclerView.Adapter<ContactViewHolder>() {
    private val inflater = LayoutInflater.from(ctx)!!


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(inflater.inflate(R.layout.contact_list_entry, parent, false))
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts.get(contacts.keyAt(position))

        holder.name.text = contact.displayName
        holder.icon.setText(contact.firstName.substring(0, 1))
        holder.header.text = contact.firstName.substring(0, 1).toUpperCase()
    }

}