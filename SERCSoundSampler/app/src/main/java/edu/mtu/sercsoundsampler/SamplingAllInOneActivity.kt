package edu.mtu.sercsoundsampler

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.mtu.sercsoundsampler.model.PreferencesHelper
import edu.mtu.sercsoundsampler.model.SoundDatabase
import edu.mtu.sercsoundsampler.model.SourceAdapter
import edu.mtu.sercsoundsampler.model.SourceListKeeper
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
    lateinit var prefs: SharedPreferences
    lateinit var helper: PreferencesHelper
    lateinit var sampler: Sampler
    lateinit var db: SoundDatabase
    lateinit var keeper: SourceListKeeper
    lateinit var adapter: SourceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate...")
        prefs = applicationContext.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)
        helper = PreferencesHelper(prefs)
        sampler = Sampler(prefs, helper)
        keeper = SourceListKeeper(helper)
        db = SoundDatabase(applicationContext.resources.getString(R.string.bad_item))
        adapter = SourceAdapter(applicationContext, prefs, helper, keeper, db)
        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.sample_and_rate_ctl_layout_ref)
        samplingSources.adapter = adapter
        "${sampler!!.getSampleSecondLength()}".also { sampleLength.setText(it) }
        "${sampler!!.getSampleSecondInterval()}".also { sampleInterval.setText(it) }
        addSource.setOnClickListener { SourceInputDialog(this, keeper).show() }
    }

    fun showNameDialog() {

    }
}