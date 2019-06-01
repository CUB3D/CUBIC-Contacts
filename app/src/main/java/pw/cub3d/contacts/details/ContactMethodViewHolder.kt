package pw.cub3d.contacts.details

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pw.cub3d.contacts.R

class ContactMethodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.methodEntry_name)!!
    val icon: ImageView = view.findViewById(R.id.methodEntry_icon)!!
}
