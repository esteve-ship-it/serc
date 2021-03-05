package edu.mtu.sercsoundsampler

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import edu.mtu.sercsoundsampler.model.PreferencesHelper
import kotlinx.android.synthetic.main.sample_and_rate_ctl_layout.*

/** SampleTypeManagerActivity - Manage a list of sounds you want to capture
 *
 *  04 Feb 2021
 *
 *
 */
class SamplingAllInOneActivity : AppCompatActivity() {
    companion object {
        val TAG = SamplingAllInOneActivity::class.java.simpleName
        val KEY_PREFS = "${TAG}.PREFS"
    }
    var sampler: Sampler? = null
    var prefs: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate...")
        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.sample_and_rate_ctl_layout_ref)
        prefs = applicationContext.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)
        sampler = Sampler(prefs!!, PreferencesHelper(prefs!!))
        "${sampler!!.getSampleSecondLength()}".also { sampleLength.setText(it) }
        "${sampler!!.getSampleSecondInterval()}".also { sampleInterval.setText(it) }
    }
}