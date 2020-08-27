package pw.cub3d.contacts.contactslist

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.abdularis.civ.AvatarImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pw.cub3d.contacts.R
import pw.cub3d.contacts.dataSources.Contact
import pw.cub3d.contacts.post

class ContactsAdapter(private val ctx: Context, private val contacts: List<Contact>): RecyclerView.Adapter<ContactViewHolder>() {
    private val inflater = LayoutInflater.from(ctx)!!

    private val backgroundColours = listOf(
        Color.rgb(200, 0, 0),
        Color.rgb(0, 200, 0),
        Color.rgb(0, 0, 200),
        Color.rgb(0, 200, 200),
        Color.rgb(200, 0, 200),
        Color.rgb(200, 200, 0)
    )


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


        if(contact.hasPhoto) {
            val bm = contact.getImageBitmap(ctx)

            holder.icon.setImageBitmap(bm)
            holder.icon.state = AvatarImageView.SHOW_IMAGE
        } else {
            holder.icon.text = contact.firstInitial
            holder.icon.state = AvatarImageView.SHOW_INITIAL
            holder.icon.avatarBackgroundColor = backgroundColours[contact.contactID.hashCode() % backgroundColours.size]

        }

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