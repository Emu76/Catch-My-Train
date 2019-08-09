package com.beachball.traincatcher.interactor

import com.beachball.traincatcher.ApiInterface
import com.beachball.traincatcher.model.Arrival
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ArrivalInteractor {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
    private var client: OkHttpClient
    private var retrofit: Retrofit
    private var service: ApiInterface

    init {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
        service = retrofit.create<ApiInterface>(ApiInterface::class.java)
    }

    fun getArrivalsByStationId(stationId: String, destinationName: String,
                               appId: String, appKey: String): Single<List<Arrival>> {
        return service.getArrivalById(stationId, false, appId, appKey)
                .flatMapPublisher {
                    Flowable.fromIterable(it)
                }
                .filter {
                    it.destinationName.contains(destinationName)
                }
                .map { it }
                .toList()
    }

    companion object {
        const val BASE_URL = "https://api.tfl.gov.uk/"
    }
}