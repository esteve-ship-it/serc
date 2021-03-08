package edu.mtu.sercsoundsampler.model

class SourceListKeeper(val helper: PreferencesHelper, val adapter: SourceAdapter) {
    companion object {
        val TAG = "SourceListKeeper"
        val KEY_SOURCE_LIST = "edu.mtu.src.list"
    }
    fun add(source: String) {
        var array: ArrayList<String> = helper.recallList(KEY_SOURCE_LIST)
        if (!array.contains(source)) {
            array.add(0, source)
            helper.preserveList(KEY_SOURCE_LIST, array)
            adapter.notifyDataSetChanged()
        }
    }
    fun list(): List<String> {
        return helper.recallList(KEY_SOURCE_LIST)
    }
    fun clear() {
        helper.clearAll()
    }
}