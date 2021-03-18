package edu.mtu.sercsoundsampler.model

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.TextView
import edu.mtu.sercsoundsampler.R

class SERCSourceAdapter(val cText: Context
                        , val prefs: SharedPreferences
                        , val helper: SERCPreferencesHelper
                        , val keeper: SourceListKeeper, val db: SERCSoundDatabase)
        : ArrayAdapter<SERCSoundEntry>(cText, R.layout.source_item_layout) {
    override fun getCount(): Int { return keeper.getCount() }
    override fun getItem(p0: Int): SERCSoundEntry { return db.getEntry(keeper.getItem(p0)) }
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = if (view != null) view else
            LayoutInflater.from(context).inflate(R.layout.source_item_layout
                    , null, false)
        var entry = db.getEntry(keeper.getItem(position))
        val nameView = rowView.findViewById<TextView>(R.id.srcTextView)
        nameView.text = entry.name
        val switch = rowView.findViewById<Switch>(R.id.srcOnOff)
        switch.setOnCheckedChangeListener { _, b -> db.sourceChanged(nameView.text.toString(), b) }
        return rowView
    }
}