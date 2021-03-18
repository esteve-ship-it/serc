package edu.mtu.sercsoundsampler.model

import edu.mtu.sercsoundsampler.services.SERCSampler


class SERCSoundDatabase(val s: String, val sampler: SERCSampler) {
    val activeSet = HashSet<String>()
    val badEntry = SERCSoundEntry(s)
    fun getEntry(sound: String?): SERCSoundEntry { return if (sound != null) SERCSoundEntry(sound!!) else badEntry }

    fun sourceChanged(src: String, onOff: Boolean) {
        if (onOff) activeSet.add(src) else activeSet.remove(src)
        sampler.mark(activeSet)
    }
}