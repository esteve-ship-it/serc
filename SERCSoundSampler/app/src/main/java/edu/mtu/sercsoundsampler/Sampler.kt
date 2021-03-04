package edu.mtu.sercsoundsampler

import android.content.SharedPreferences
import edu.mtu.sercsoundsampler.model.PreferencesHelper

class Sampler(val prefs: SharedPreferences, val helper: PreferencesHelper) {
    var sources: HashSet<String> = HashSet()
    companion object REF {
        val TAG = "Sampler"
        val KEY_SAMPLE_LENGTH = "serc.sample.len.msecs"
        val KEY_SAMPLE_INTERVAL = "serc.sample.interval.msecs"
        val DEFAULT_SAMPLE_LENGTH_SECONDS: Long = 4
        val DEFAULT_SAMPLE_INTERVAL_SECONDS: Long = 60
        //var sampleLengthInMilliseconds = DEFAULT_SAMPLE_LENGTH_MS
    //var sampleIntervalInMilliseconds = DEFAULT_SAMPLE_INTERVAL_MS
    val emptySet = HashSet<String>()
    var sampling = false
}

    /** mark - mark the current sample as contains the sources sent in as the 'srcs' argument.
     *         If in between samples start one immediately
     *
     */
    fun mark(srcs: Set<String>) {
        sources.clear()
        sources.addAll(srcs)
        takeSample()
    }

    fun getSampleSecondLength(): Long {
        return prefs.getLong(KEY_SAMPLE_LENGTH, DEFAULT_SAMPLE_LENGTH_SECONDS)
    }

    fun getSampleSecondInterval(): Long {
        return prefs.getLong(KEY_SAMPLE_INTERVAL, DEFAULT_SAMPLE_INTERVAL_SECONDS)
    }

    fun setSampleSecondLength(len: Long) {
        if (len > 0 && len < getSampleSecondInterval())
            helper.preserveLong(KEY_SAMPLE_LENGTH, len)
    }
    fun setSampleSecondInterval(i: Long) {
        if (i > 0) helper.preserveLong(KEY_SAMPLE_INTERVAL, i)
        if (getSampleSecondLength() >= getSampleSecondInterval())
            helper.preserveLong(KEY_SAMPLE_LENGTH, getSampleSecondInterval() - 1)
    }

    /** If the app is not active, then we can't know if samples taken while running in the background
     * have *any* designated sources.
     */
    fun onDestroy() { mark(emptySet) }

    fun takeSample() {
        if (!sampling) {
            sampling = true
        }
    }
    fun run() {
        
    }
}