package pw.cub3d.contacts

import org.greenrobot.eventbus.EventBus

fun Any.post() {
    EventBus.getDefault().post(this)
}

fun Any.register() {
    EventBus.getDefault().register(this)
}

fun Any.unregister() {
    EventBus.getDefault().unregister(this)
}