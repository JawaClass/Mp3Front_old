package com.example.a18mas.mp3front.helper

import android.util.Log
import com.example.a18mas.mp3front.models.SearchResult
import com.example.a18mas.mp3front.UI.myContext
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL
import kotlinx.coroutines.experimental.async
import java.io.File
import java.net.ConnectException

class HttpClient() {

    private val jsonFactory = Gson()
    val DOWNLOAD_DIR_PATH = "/sdcard/Music/MP3DOWNLOADS/"


    fun preQuest() {
        if (!File(DOWNLOAD_DIR_PATH).isDirectory)
            File(DOWNLOAD_DIR_PATH).mkdir()
    }

    init {
        Log.i("HTTP_CLIENT", "INIT")
        preQuest()


    }

    private var myHttpClientListener: MyHttpClientListener? = null

    fun setOnDoneSuccess(eventListener: MyHttpClientListener) {
        this.myHttpClientListener = eventListener
    }


    //


    fun download(idTitles: ArrayList<Pair<*, *>>) {
        for (idTitle in idTitles) {
            var url = "http://192.168.178.63:5545/download?id=${idTitle.first}&title=${(idTitle.second).toString().replace(" ", "+")}"
            Fuel.download(url).destination { response, url ->
                File.createTempFile(idTitle.second as String, ".webm", File(DOWNLOAD_DIR_PATH))
            }.response { req, res, result ->

                when (result) {
                    is Result.Failure -> {
                        Log.d("HTTP_CLIENT", "nicht ok:" + result.getException().toString())
                    }
                    is Result.Success -> {
                        Log.d("HTTP_CLIENT", "OK.")
                        myContext?.makeLongToastMessage("File \"${idTitle.second}\"stored in ${DOWNLOAD_DIR_PATH}")
                    }

                }

            }

        }
    }

    //

    fun fetchSearchResultData(query: String) {
        Log.i("Request", "fetchSearchResultData")
        query.replace(" ", "+")
        var endpoint = ""
        when (searchMode) {
            0 -> {
                endpoint = "http://192.168.178.63:5545/apisearch?q="

            }
            1 -> {
                endpoint = "http://192.168.178.63:5545/artist?q="
            }
            else -> {
            }

        }

        async {

            val result = try {
                URL(endpoint + query.replace(" ", "+")).readText()

            } catch (e: ConnectException) {
                e
            }

            when (result) {
                is String -> {
                    Log.i("HTTP_CLIENT", "parse json")
                    try {
                        val listSearchResult = jsonFactory.fromJson<Array<SearchResult>>(result)
                        myHttpClientListener?.OnDoneSuccess(listSearchResult) {}

                    } catch (e: Exception) {
                        Log.i("EXCEPTION JSON", "says: $e")

                    }


                }
                is ConnectException -> {
                    myHttpClientListener?.OnFailed(result) {}
                }
                else -> {
                    /// ...
                }
            }


        }

    }

    // https://kotlinlang.org/docs/reference/inline-functions.html
    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

}