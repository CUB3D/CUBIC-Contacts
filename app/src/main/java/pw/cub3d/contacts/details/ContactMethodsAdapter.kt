package pw.cub3d.contacts.details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pw.cub3d.contacts.R
import pw.cub3d.contacts.dataSources.ContactMethod

class ContactMethodsAdapter(ctx: Context, private val methods: List<ContactMethod>) : RecyclerView.Adapter<ContactMethodViewHolder>() {
    private val inflater = LayoutInflater.from(ctx)!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactMethodViewHolder {
        return ContactMethodViewHolder(inflater.inflate(R.layout.contact_method_entry, parent, false))
    }

    override fun getItemCount(): Int {
        return methods.size
    }

    override fun onBindViewHolder(holder: ContactMethodViewHolder, position: Int) {
        val method = methods[position]

        holder.icon.setImageResource(method.icon_id)
        holder.name.text = method.name
    }
}
