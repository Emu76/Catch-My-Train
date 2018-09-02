package com.beachball.traincatcher.model

import com.google.gson.annotations.SerializedName

data class Arrival(
        @SerializedName("\$type") val type: String,
        @SerializedName("id") val id: String,
        @SerializedName("operationType") val operationType: Int,
        @SerializedName("vehicleId") val vehicleId: String,
        @SerializedName("naptanId") val naptanId: String,
        @SerializedName("stationName") val stationName: String,
        @SerializedName("lineId") val lineId: String,
        @SerializedName("lineName") val lineName: String,
        @SerializedName("platformName") val platformName: String,
        @SerializedName("direction") val direction: String,
        @SerializedName("bearing") val bearing: String,
        @SerializedName("destinationNaptanId") val destinationNaptanId: String,
        @SerializedName("destinationName") val destinationName: String,
        @SerializedName("timestamp") val timestamp: String,
        @SerializedName("timeToStation") val timeToStation: Int,
        @SerializedName("currentLocation") val currentLocation: String,
        @SerializedName("towards") val towards: String,
        @SerializedName("expectedArrival") val expectedArrival: String,
        @SerializedName("timeToLive") val timeToLive: String,
        @SerializedName("modeName") val modeName: String,
        @SerializedName("timing") val timing: Timing
        )

data class Timing(
        @SerializedName("\$type") val type: String,
        @SerializedName("countdownServerAdjustment") val countdownServerAdjustment: String,
        @SerializedName("source") val source: String,
        @SerializedName("insert") val insert: String,
        @SerializedName("read") val read: String,
        @SerializedName("sent") val sent: String,
        @SerializedName("received") val received: String
)