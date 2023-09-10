package com.gps_navigator.model.delegates

import android.content.Context
import android.preference.PreferenceManager
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.mp.KoinPlatform
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreferencesDelegate(private val name: String) :
    ReadWriteProperty<Any?, MutableList<MarkerOptions>?> {
    private val appSharedPrefs =
        PreferenceManager.getDefaultSharedPreferences(
            KoinPlatform.getKoin().get<Context>().applicationContext
        )
    private val gson by lazy { Gson() }

    override fun getValue(thisRef: Any?, property: KProperty<*>): MutableList<MarkerOptions> {
        val type: Type = object : TypeToken<MutableList<MarkerOptions?>?>() {}.type
        val gsonString = appSharedPrefs.getString(name, "[]")
        return gson.fromJson(gsonString, type)
    }

    override fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: MutableList<MarkerOptions>?
    ) {
        val prefsEditor = appSharedPrefs.edit()
        val jsonStr = gson.toJson(value)
        prefsEditor.putString(name, jsonStr)
        prefsEditor.apply()
    }
}