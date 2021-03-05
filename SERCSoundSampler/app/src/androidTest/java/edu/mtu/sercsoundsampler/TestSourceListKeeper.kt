package edu.mtu.sercsoundsampler

import android.content.Context
import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import edu.mtu.sercsoundsampler.model.PreferencesHelper
import edu.mtu.sercsoundsampler.model.SourceAdapter
import edu.mtu.sercsoundsampler.model.SourceListKeeper

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TestSourceListKeeper {
    val prefs: SharedPreferences
    val adapter: SourceAdapter
    val helper: PreferencesHelper
    val keeper: SourceListKeeper
    init {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        prefs = appContext.getSharedPreferences(SamplingAllInOneActivity.KEY_PREFS, Context.MODE_PRIVATE)
        adapter = SourceAdapter(prefs)
        helper = PreferencesHelper(prefs)
        keeper = SourceListKeeper(helper, adapter)
    }
    @Before
    fun setUp() {
        helper.clearAll()
    }
    @Test
    fun ignoreDuplicateAdd() {
        keeper.add("Fan")
        keeper.add("Fan")
        keeper.add("Lawnmower")
        assertEquals("Expect 2", 2, keeper.list().size)
    }
}