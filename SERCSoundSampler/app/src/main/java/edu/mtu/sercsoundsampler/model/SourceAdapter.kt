package edu.mtu.sercsoundsampler.model

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import edu.mtu.sercsoundsampler.R

class SourceAdapter(val cText: Context
                    , val prefs: SharedPreferences
                    , val helper: PreferencesHelper
                    , val keeper: SourceListKeeper, val db: SoundDatabase)
        : ArrayAdapter<SoundEntry>(cText, R.layout.source_item_layout) {
    override fun getCount(): Int { return keeper.getCount() }
    override fun getItem(p0: Int): SoundEntry { return db.getEntry(keeper.getItem(p0)) }
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = if (view != null) view else
            LayoutInflater.from(context).inflate(R.layout.source_item_layout
                    , null, false)
        var entry = db.getEntry(keeper.getItem(position))
        val nameView = rowView.findViewById<TextView>(R.id.srcTextView)
        nameView.text = entry.name
        return rowView
    }
}