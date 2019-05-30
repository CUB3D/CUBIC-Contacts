package pw.cub3d.contacts.contactslist

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.abdularis.civ.AvatarImageView
import com.github.abdularis.civ.CircleImageView
import pw.cub3d.contacts.R

class ContactViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val root: View = view.findViewById(R.id.contact_rootview)!!

    val name: TextView = view.findViewById(R.id.contact_name)!!
    val header: TextView = view.findViewById(R.id.contact_header)!!

    val icon: AvatarImageView = view.findViewById(R.id.contact_avatarIcon)!!
}