package edu.mtu.sercsoundsampler

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import edu.mtu.sercsoundsampler.model.SERCPreferencesHelper
import edu.mtu.sercsoundsampler.model.SERCSoundDatabase
import edu.mtu.sercsoundsampler.model.SERCSourceAdapter
import edu.mtu.sercsoundsampler.model.SourceListKeeper
import edu.mtu.sercsoundsampler.services.SERCSampler
import kotlinx.android.synthetic.main.sample_and_rate_ctl_layout.*

/** SampleTypeManagerActivity - Manage a list of sounds you want to capture
 *
 *  04 Feb 2021
 *
 *
 */
class SERCSamplingAllInOneActivity : AppCompatActivity() {
    companion object {
        val TAG = SERCSamplingAllInOneActivity::class.java.simpleName
        val KEY_PREFS = "${TAG}.PREFS"
    }
    lateinit var prefs: SharedPreferences
    lateinit var helper: SERCPreferencesHelper
    lateinit var sampler: SERCSampler
    lateinit var db: SERCSoundDatabase
    lateinit var keeper: SourceListKeeper
    lateinit var adapter: SERCSourceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate...")
        prefs = applicationContext.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)
        helper = SERCPreferencesHelper(prefs)
        sampler = SERCSampler(prefs, helper)
        keeper = SourceListKeeper(helper)
        db = SERCSoundDatabase(applicationContext.resources.getString(R.string.bad_item), sampler)
        adapter = SERCSourceAdapter(applicationContext, prefs, helper, keeper, db)
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