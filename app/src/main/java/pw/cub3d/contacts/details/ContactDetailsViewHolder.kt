package pw.cub3d.contacts.details

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pw.cub3d.contacts.R

class ContactDetailsViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val value = view.findViewById<TextView>(R.id.contactDetail_value)!!
    val icon = view.findViewById<ImageView>(R.id.contactDetail_icon)!!
    val title = view.findViewById<TextView>(R.id.contactDetail_title)!!
}
