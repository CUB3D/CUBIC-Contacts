package pw.cub3d.contacts

import androidx.fragment.app.FragmentTransaction

inline fun FragmentTransaction.use(crossinline r:(FragmentTransaction)->Unit) {
    r(this)
    this.commit()
}