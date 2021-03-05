package edu.mtu.sercsoundsampler.model

import android.content.SharedPreferences
import java.util.*
import kotlin.collections.ArrayList

class PreferencesHelper(val prefs: SharedPreferences) {
    fun preserveString(key: String, value: String) { prefs.edit().putString(key, value)!!.commit() }
    fun preserveLong(key: String, value: Long) { prefs.edit().putLong(key, value)!!.commit() }

    fun recallIndex(pref: String, arr: List<String>, defaultInt: Int): Int {
        var value: String? = prefs.getString(pref, "")
        return if (value != "") indexWithDefault(value!!, arr, defaultInt) else defaultInt
    }
    fun indexWithDefault(value: String, arr: List<String>, defaultInt: Int): Int {
        var rv: Int = if (arr.indexOf(value) >= 0) arr.indexOf(value) else defaultInt
        return rv
    }

    /*
    add: key.fan -> fan,0
    add key.lawnmower -> lawnmower,0; fan,1
    0,fan
    0,lawnmower; 1,fan
     */
    fun preserveList(key: String, array: List<String>) {
        array.forEachIndexed { i, s -> prefs.edit().putInt("${key}.${s}",i)!!.commit() }
        val ts = TreeSet<String>(array)
        prefs.edit().putStringSet(key, ts)!!.commit()
    }
    fun recallList(key: String): ArrayList<String> {
        var rv = ArrayList<String>()
        var ts = TreeSet<String>(prefs.getStringSet(key, TreeSet<String>()))
        val tm = TreeMap<Int, String>()
        for (s in ts) {
            val i = prefs.getInt("${key}.${s}", -1)
            if (i != -1) tm.put(i, s)
        }
        return ArrayList(tm.values)
    }
    fun clearAll() { prefs.edit().clear().apply() }

}