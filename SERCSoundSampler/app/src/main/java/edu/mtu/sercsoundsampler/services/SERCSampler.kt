package edu.mtu.sercsoundsampler.services

import android.Manifest
import android.content.SharedPreferences
import android.location.Location
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import edu.mtu.sercsoundsampler.MultiListener
import edu.mtu.sercsoundsampler.model.SERCPreferencesHelper
import kotlinx.android.synthetic.main.sample_layout.*
import kotlinx.coroutines.*

import java.io.File
import java.io.PrintWriter
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

class SERCSampler(val prefs: SharedPreferences
                , val helper: SERCPreferencesHelper, val multiListener: MultiListener) {
    var sources: HashSet<String> = HashSet()
    companion object {
        val TAG = "Sampler"
        val KEY_SAMPLE_LENGTH = "serc.sample.len.msecs"
        val KEY_SAMPLE_INTERVAL = "serc.sample.interval.msecs"
        val DEFAULT_SAMPLE_LENGTH_SECONDS: Long = 4
        val DEFAULT_SAMPLE_INTERVAL_SECONDS: Long = 60
        val DEFAULT_SAMPLE_RATE: Int = 44100
        var isSampling = false
        //var sampleLengthInMilliseconds = DEFAULT_SAMPLE_LENGTH_MS
        //var sampleIntervalInMilliseconds = DEFAULT_SAMPLE_INTERVAL_MS
    }
    val emptySet = HashSet<String>()
    var enableSampling = true

    suspend fun performSample(sampleLengthSeconds: Long, intervalSeconds: Long) {
        Log.i(TAG, "Coroutine to collect sample started...")
        while (enableSampling) {
            delay((intervalSeconds - sampleLengthSeconds) * 1000)
            sample(sampleLengthSeconds)
        }
        Log.i(TAG, "Coroutine to collect sample stopped...")
    }

    private fun addEntry(mappingFile: File, sounds: Set<String>, audioFilename: String, location: Location) {
        val w: PrintWriter = PrintWriter(mappingFile)
        w.append(audioFilename)
                .append(",[")
                .append(csvOf(sounds))
                .append("],")
                .append(location.latitude.toString())
                .append(",")
                .append(location.longitude.toString())
                .append("\n")
        w.close()
    }

    fun csvOf(sounds: Set<String>): String {
        val sb = StringBuilder()
        sounds.map { sb.append(",").append(it)}
        return sb.toString().substring(1)
    }

    /** When we sample, we want the sound in a *.wav file and an entry added to the metadata file
     * including the sounds that are noted by the user as being active.
     */
    private fun sample(sampleLengthSeconds: Long) {

        var audioDirPath: File? = Environment.getDataDirectory()
        var mappingFile: File? = null
        var dateFilename = ""
        var sd: File? = null
        try {
            isSampling = true
            var recorder = MyRecorder(sampleLengthSeconds.toInt(), DEFAULT_SAMPLE_RATE)
            var location = Location("me")
            if (multiListener.proceed) {
                var date = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())
                dateFilename = date.toString() + ".pcm"
                Log.i(TAG, "Recording " + dateFilename)
                sd = File(audioDirPath, dateFilename)
                recorder.run(sd!!, TAG)
                addEntry(mappingFile!!, sources, dateFilename, location)
//                    mappingFile!!.appendText(temporyTitleForButton + "," + audioFile + "\n")

            } else {
                recorder.interrupt()
            }
        } catch (e: java.lang.Exception){
            Log.d(TAG, e.toString());
        } finally {
            isSampling = false
        }
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
        if (!isSampling) {
            sample(getSampleSecondLength())
        }
    }
}

class MyRecorder(val sampleLengthSeconds: Int, val sampleRateHz: Int) : Thread() {
    private val bufferSize =  sampleLengthSeconds * sampleRateHz
    private val aformat = AudioFormat.Builder().setSampleRate(sampleRateHz).setEncoding(AudioFormat.ENCODING_PCM_16BIT).setChannelMask(
            AudioFormat.CHANNEL_IN_MONO).build();
    private val audioRecorder = AudioRecord.Builder().setBufferSizeInBytes(bufferSize).setAudioFormat(aformat).setAudioSource(
            MediaRecorder.AudioSource.MIC).build();

    var x = 0
    //comment
    fun run(filename: File, tag: String) {
        var data = ByteArray(bufferSize)
        audioRecorder.startRecording()
        audioRecorder.read(data,0,bufferSize)
        filename.writeBytes(data)
    }
}

class MultiListener : MultiplePermissionsListener {
    var proceed: Boolean = false
    override fun onPermissionsChecked(prt: MultiplePermissionsReport) { proceed = true }
    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
    }
}