package pw.cub3d.contacts.details

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pw.cub3d.contacts.R

class ContactMethodViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
    val name: TextView = root.findViewById(R.id.methodEntry_name)!!
    val icon: ImageView = root.findViewById(R.id.methodEntry_icon)!!
}
