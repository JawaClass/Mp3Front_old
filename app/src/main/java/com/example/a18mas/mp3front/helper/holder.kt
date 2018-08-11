package com.example.a18mas.mp3front.helper

import android.support.design.widget.Snackbar
import com.example.a18mas.mp3front.models.SearchResult


import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.a18mas.mp3front.MyExoPlayer


import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

//  [0] nach Title
//  [1] nach Artist
var searchMode: Int = 0

var currentMeta: SearchResult? = null
var playerImpl: MyExoPlayer? = null
var httpClient: HttpClient? = null

/////////////////////////////////////

fun streamMP3(meta: SearchResult) {
    currentMeta = meta
    var strUri = "http://192.168.178.63:5545/streamX?id=${meta.video_id}"
    playerImpl?.setNewSource(strUri)
    Log.i("HELP", "new Source is set $strUri.")
    Log.i("HELP", "player is init ${playerImpl != null}")
}


/*
var listData: Array<SearchResult>? = arrayOf(
        SearchResult("1:40", 100, "X93", "https://vignette.wikia.nocookie.net/shipping/images/9/91/Tokyoghoul.jpg/revision/latest?cb=20170915212853", "tokyo ghoul"),
        SearchResult("3:20", 100, "X92", "https://vignette.wikia.nocookie.net/shipping/images/9/91/Tokyoghoul.jpg/revision/latest?cb=20170915212853", "naruto"),
        SearchResult("2:14", 100, "X99", "https://vignette.wikia.nocookie.net/shipping/images/9/91/Tokyoghoul.jpg/revision/latest?cb=20170915212853", "saskue"),
        SearchResult("3:20", 100, "X92", "https://vignette.wikia.nocookie.net/shipping/images/9/91/Tokyoghoul.jpg/revision/latest?cb=20170915212853", "naruto"),
        SearchResult("3:20", 100, "X92", "https://vignette.wikia.nocookie.net/shipping/images/9/91/Tokyoghoul.jpg/revision/latest?cb=20170915212853", "naruto"),
        SearchResult("3:20", 100, "X92", "https://vignette.wikia.nocookie.net/shipping/images/9/91/Tokyoghoul.jpg/revision/latest?cb=20170915212853", "naruto"),
        SearchResult("3:20", 100, "X92", "https://vignette.wikia.nocookie.net/shipping/images/9/91/Tokyoghoul.jpg/revision/latest?cb=20170915212853", "naruto"),
        SearchResult("3:20", 100, "X92", "https://vignette.wikia.nocookie.net/shipping/images/9/91/Tokyoghoul.jpg/revision/latest?cb=20170915212853", "naruto"),
        SearchResult("3:20", 100, "X92", "https://vignette.wikia.nocookie.net/shipping/images/9/91/Tokyoghoul.jpg/revision/latest?cb=20170915212853", "naruto"),
        SearchResult("3:20", 100, "X92", "https://vignette.wikia.nocookie.net/shipping/images/9/91/Tokyoghoul.jpg/revision/latest?cb=20170915212853", "naruto"),
        SearchResult("3:20", 100, "X92", "https://vignette.wikia.nocookie.net/shipping/images/9/91/Tokyoghoul.jpg/revision/latest?cb=20170915212853", "naruto"),
        SearchResult("3:20", 100, "X92", "https://vignette.wikia.nocookie.net/shipping/images/9/91/Tokyoghoul.jpg/revision/latest?cb=20170915212853", "naruto")

        )
*/
fun View.makeSnackbarMessage(message: String) = Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
//   .setAction("Action", null).show()


fun Context.makeToastMessage(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


fun Context.makeLongToastMessage(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()


