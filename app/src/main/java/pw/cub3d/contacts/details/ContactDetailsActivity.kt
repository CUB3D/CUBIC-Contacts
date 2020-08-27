package pw.cub3d.contacts.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.abdularis.civ.AvatarImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pw.cub3d.contacts.R

//import kotlinx.android.synthetic.main.activity_contact_details.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import pw.cub3d.contacts.contactEdit.EditContactActivity
import pw.cub3d.contacts.dataSources.ContactMethods
import pw.cub3d.contacts.dataSources.ContactResponse
import pw.cub3d.contacts.dataSources.Contacts
import pw.cub3d.contacts.dataSources.IContactRepository
import pw.cub3d.contacts.register
import pw.cub3d.contacts.unregister

class ContactDetailsActivity : AppCompatActivity() {

    val contacts: IContactRepository by inject()
    val contactMethods: ContactMethods by inject()

    val contact_back: ImageButton by lazy {
        findViewById(R.id.contact_back)
    }

    val contact_avatarIcon: AvatarImageView by lazy {
        findViewById(R.id.contact_avatarIcon)
    }
    val contact_name: TextView by lazy {
        findViewById(R.id.contact_name)
    }
    val contact_phonetic_name: TextView by lazy {
        findViewById(R.id.contact_phonetic_name)
    }
    val contact_contactMethods: TableLayout by lazy {
        findViewById(R.id.contact_contactMethods)
    }

    val contact_contactDetails: RecyclerView by lazy {
        findViewById(R.id.contact_contactDetails)
    }
    val contact_editButton: Button by lazy {
        findViewById(R.id.contact_editButton)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

        contact_back.setOnClickListener { finish() }

        register()

        intent.getIntExtra(CONTACT_ID, -1).takeIf {
            it > 0
        }?.let {
            GlobalScope.launch {
                contacts.getContactById(it).collect {
                    it?.let {
                        runOnUiThread {
                            onContactCallback(ContactResponse(it))
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        unregister()
        super.onDestroy()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onContactCallback(contact: ContactResponse) {
        val con = contact.contact

        if(con.hasPhoto) {
            contact_avatarIcon.setImageBitmap(con.getImageBitmap(this))
            contact_avatarIcon.state = AvatarImageView.SHOW_IMAGE
        } else {
            contact_avatarIcon.text = con.firstInitial
        }
        contact_name.text = con.displayName
        contact_phonetic_name.text = con.phoneticName

        val row = contact_contactMethods[0] as TableRow

        val i = LayoutInflater.from(this)
        for(method in contactMethods.ALL_CONTACT_METHODS) {
            val onClickAction = method.launchIntent(con) ?: continue

            val v = i.inflate(R.layout.contact_method_entry, row, false)
            val vh = ContactMethodViewHolder(v)

            vh.root.setOnClickListener {
                this@ContactDetailsActivity.startActivity(onClickAction)
            }
            vh.name.text = method.name
            vh.icon.setImageResource(method.icon_id)

            row.addView(v)
        }
        contact_contactMethods.isStretchAllColumns = true


        contact_contactDetails.layoutManager = LinearLayoutManager(this)
        contact_contactDetails.adapter = ContactDetailsAdapter(this, contact.contact.details)

        contact_editButton.setOnClickListener {
            startActivity(Intent(this, EditContactActivity::class.java))
        }
    }

    companion object {
        const val VIEW_CONTACT: Int = 1
        const val CONTACT_ID = "CONTACT_ID"
    }
}
