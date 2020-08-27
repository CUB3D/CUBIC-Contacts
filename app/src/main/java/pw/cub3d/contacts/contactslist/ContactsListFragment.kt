package pw.cub3d.contacts.contactslist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.fragment_contacts_list.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject

import pw.cub3d.contacts.R
import pw.cub3d.contacts.dataSources.Contacts
import pw.cub3d.contacts.dataSources.ContactsResponse
import pw.cub3d.contacts.register
import pw.cub3d.contacts.unregister

class ContactsListFragment : Fragment() {


    val recycler_contacts: RecyclerView by lazy {
        requireView().findViewById(R.id.recycler_contacts) as RecyclerView
    }

    val contacts: Contacts by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts_list, container, false)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onContactsLoaded(rsp: ContactsResponse) {
        recycler_contacts.adapter = ContactsAdapter(requireContext(), rsp.contacts.sortedBy { it.firstName.toLowerCase() })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregister()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        register()

        recycler_contacts.layoutManager = LinearLayoutManager(requireContext())

        contacts.requestContacts()
    }

}
