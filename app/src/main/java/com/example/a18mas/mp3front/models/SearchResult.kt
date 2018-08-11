package com.example.a18mas.mp3front.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

/*
@Parcelize
data class SearchResult(
        var duration :String,
        var durationseconds :Int,
        var id :String,
        var thumbnail :String,
        var title :String): Parcelable
        */


@Parcelize
data class SearchResult(
        @SerializedName("video_id")
        var video_id: String,
        @SerializedName("title")
        var title: String,
        @SerializedName("duration")
        var duration: String,
        @SerializedName("duration_seconds")
        var duration_seconds: Int,
        @SerializedName("duration_formatted")
        var duration_formatted: String,
        @SerializedName("description")
        var description: String,
        @SerializedName("channel_id")
        var channel_id: String,
        @SerializedName("channel_title")
        var channel_title: String,
        @SerializedName("thumbnail")
        var thumbnail: Map<String, String>,
        @SerializedName("published_at")
        var published_at: String,
        // exclude from json modell
        var  checked: Boolean
        ) : Parcelable




