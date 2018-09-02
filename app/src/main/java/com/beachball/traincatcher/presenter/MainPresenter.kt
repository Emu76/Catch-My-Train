package com.beachball.traincatcher.presenter

import com.beachball.traincatcher.interactor.ArrivalInteractor
import com.beachball.traincatcher.model.Arrival
import com.beachball.traincatcher.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter(var mainView: MainView?) {

    private val interactor: ArrivalInteractor = ArrivalInteractor()
    private val list: MutableList<Arrival> = ArrayList()

    fun getArrivals() {
        interactor.getArrivalsByStationId("940GZZDLSIT")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    list.add(it)
                } , {
                } , {
                    sortList()
                }
        )
    }

    private fun sortList() {
        if(list.size > 0) {
            list.sortBy {
                it.timeToStation
            }
            mainView?.presentNextArrival(list[0].timeToStation)
        }
    }

    fun onDestroy() {
        mainView = null
    }
}