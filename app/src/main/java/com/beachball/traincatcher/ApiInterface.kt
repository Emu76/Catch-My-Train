package com.beachball.traincatcher

import com.beachball.traincatcher.model.Arrival
import io.reactivex.Single
import retrofit2.http.*

interface ApiInterface {

    @GET("StopPoint/{id}/Arrivals")
    fun getArrivalById(@Path("id") stopId: String,
                       @Query("includeCrowdingData") crowdData: Boolean,
                       @Header("app_id") appId: String,
                       @Header("app_key") appKey: String): Single<List<Arrival>>
}