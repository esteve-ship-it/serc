package edu.mtu.sercsoundsampler.model

import android.app.Activity
import android.content.Context
import edu.mtu.sercsoundsampler.R


class SoundDatabase(val s: String) {
    val badEntry = SoundEntry(s)
    fun getEntry(sound: String?): SoundEntry { return if (sound != null) SoundEntry(sound!!) else badEntry }
}