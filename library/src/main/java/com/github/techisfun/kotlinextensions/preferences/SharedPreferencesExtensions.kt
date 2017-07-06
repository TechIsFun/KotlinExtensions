package com.github.techisfun.kotlinextensions.preferences

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Andrea Maglie on 06/07/17.
 */

/**
 * Example usage:
 * {@code
 * class MyClass(prefs: SharedPreferences) {
 * var count by prefs.int()
 * }
 * }
 *
 * Another example:
 * {@
 * class TokenHolder(prefs: SharedPreferences) {
 * var token by prefs.string()
 * private set
 *
 * var count by prefs.int()
 * private set
 *
 * fun saveToken(newToken: String) {
 * token = newToken
 * count++
 * }
 * }
 * }
 */
fun SharedPreferences.int(def: Int = 0, key: String? = null) =
        delegate(def, key, SharedPreferences::getInt, SharedPreferences.Editor::putInt)

/**
 * Example usage:
 * {@code
 * class MyClass(prefs: SharedPreferences) {
 * var millis by prefs.long()
 * }
 * }
 */
fun SharedPreferences.long(def: Long = 0, key: String? = null) =
        delegate(def, key, SharedPreferences::getLong, SharedPreferences.Editor::putLong)

fun SharedPreferences.string(def: String = "", key: String? = null) =
        delegate(def, key, SharedPreferences::getString, SharedPreferences.Editor::putString)

private inline fun <T> SharedPreferences.delegate(
        defaultValue: T,
        key: String?,
        crossinline getter: SharedPreferences.(String, T) -> T,
        crossinline setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>) =
                getter(key ?: property.name, defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>,
                              value: T) =
                edit().setter(key ?: property.name, value).apply()
    }
}