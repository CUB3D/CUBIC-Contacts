package pw.cub3d.contacts

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import pw.cub3d.contacts.contactslist.ContactSelectedEvent
import pw.cub3d.contacts.details.ContactDetailsActivity

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onContactSelected(contact: ContactSelectedEvent) {

        val intent = Intent(this, ContactDetailsActivity::class.java)
        intent.putExtra("CONTACT_ID", contact.contact.id)
        startActivity(intent)

//        supportFragmentManager.beginTransaction().use {
//            it.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
//            it.replace(R.id.fragment, ContactDetails.createInstance(contact.contact), "contact-details")
//        }
        println("SELECTED ${contact.contact.displayName}")
    }

    override fun onStart() {
        super.onStart()
        register()
    }

    override fun onStop() {
        super.onStop()
        unregister()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.menu.getItem(0).isChecked = true

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
