package com.example.a18mas.mp3front

///



import android.net.Uri
import android.os.Handler
import android.util.Log
import com.example.a18mas.mp3front.UI.myContext
import com.example.a18mas.mp3front.helper.PlayerEventListener
import com.example.a18mas.mp3front.helper.currentMeta
import com.example.a18mas.mp3front.helper.playerImpl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.experimental.Runnable

class MyExoPlayer {
    var exoPlayer : ExoPlayer? = null
    var mediaSource: MediaSource? = null
    var bandwidthMeter: BandwidthMeter? = null
    var audioTrackSelectionFactory: TrackSelection.Factory? = null
    var trackSelector: TrackSelector? = null
    var dataSourceFactory: DefaultDataSourceFactory? = null

    val TAG = "PLAYER"
    var status = 0 // 0 = pause, 1 = play, 2 = complete
    private var playerEventListener: PlayerEventListener? = null
    private var handler: Handler? = null



    fun setOnSetNewDataSourceEventListener(playerEventListener: PlayerEventListener) {
        this.playerEventListener = playerEventListener
    }


    val eventHandler = object : Runnable {
        override fun run() {
            //  Log.i(TAG, "is_complete? ${getCurrentPos()} >= ${currentMeta?.durationseconds as Int * 1000}")
            if (getCurrentPos() >= currentMeta?.duration_seconds as Int * 1000)
                playerEventListener?.OnCompletion{}
            handler?.postDelayed(this, 100)

        }
    }

    init {
        // 1.
        bandwidthMeter = DefaultBandwidthMeter()
        audioTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        trackSelector =  DefaultTrackSelector(audioTrackSelectionFactory)
        exoPlayer = ExoPlayerFactory.newSimpleInstance(myContext, trackSelector)
        Log.i(TAG, "exoplayer created.")
        dataSourceFactory =  DefaultDataSourceFactory(myContext,
                Util.getUserAgent(myContext, "app_mp3"))
        exoPlayer?.playWhenReady = true

        playerImpl = this


    }



    fun getCurrentPos(): Int {
        return (exoPlayer?.currentPosition?.toInt() as Int)
    }

    fun setNewSource(strUri: String) {
        exoPlayer?.playWhenReady = true
        mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(strUri))
        exoPlayer?.prepare(mediaSource)
        Thread.sleep(5000)
        status = 1
        playerEventListener?.OnSetNewDataSource{}

        if (handler == null) {
            handler = Handler()
            handler?.post(eventHandler)
        }

    }

    fun seekTo(positionMS: Long) {
        exoPlayer?.seekTo(positionMS)
        if (status == 0)
            toggle()

    }

    fun toggle() {
        Log.i(TAG, "state = ${exoPlayer?.playbackState}")
        if (status == 0)
            play()
        else
            pause()
    }

    fun play() {
        exoPlayer?.playWhenReady = true
        status = 1
    }

    fun pause() {
        exoPlayer?.playWhenReady = false
        status = 0
    }

}