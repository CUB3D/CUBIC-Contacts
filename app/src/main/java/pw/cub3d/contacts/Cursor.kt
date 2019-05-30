package pw.cub3d.contacts

import android.database.Cursor

fun Cursor.getStringValue(key: String): String? {
    return getString(getColumnIndex(key))
}

fun Cursor.getIntValue(key: String): Int {
    return getInt(getColumnIndex(key))
}