package pw.cub3d.contacts.details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pw.cub3d.contacts.R
import pw.cub3d.contacts.dataSources.ContactDetail

class ContactDetailsAdapter(ctx: Context, val details: List<ContactDetail>) : RecyclerView.Adapter<ContactDetailsViewHolder>() {
    private val inflater = LayoutInflater.from(ctx)!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactDetailsViewHolder {
        return ContactDetailsViewHolder(inflater.inflate(R.layout.contact_details_entry, parent, false))
    }

    override fun getItemCount() = details.size

    override fun onBindViewHolder(holder: ContactDetailsViewHolder, position: Int) {
        val detail = details[position]

        holder.icon.setImageResource(detail.icon)
        holder.title.text = detail.title
        holder.value.text = detail.value
    }
}
