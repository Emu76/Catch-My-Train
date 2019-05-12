package com.beachball.traincatcher.presenter

import android.os.Handler
import com.beachball.traincatcher.interactor.ArrivalInteractor
import com.beachball.traincatcher.model.Arrival
import com.beachball.traincatcher.util.DateUtil
import com.beachball.traincatcher.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class MainPresenter(private var mainView: MainView?) {

    private val interactor: ArrivalInteractor = ArrivalInteractor()
    private lateinit var list: List<Arrival>
    private val handler = Handler()
    private val cd: CompositeDisposable = CompositeDisposable()

    fun getArrivals(stationCode: String, destinationName: String) {
        cd.add(interactor.getArrivalsByStationId(stationCode, destinationName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mainView?.presentLoading()
                }
                .subscribe({
                    list = it
                    sortList(destinationName)
                }, {
                }
        ))
    }

    fun startCountdownJob(seconds: Int) {
        val runnable = Runnable {
            countdownJob(seconds)
        }
        handler.postDelayed(runnable, 1000)
    }

    private fun countdownJob(seconds: Int) {
        val newSeconds = seconds - 1
        if (newSeconds % 60 == 0) {
            mainView?.presentNextMinute(seconds)
        }
        if (newSeconds <= 0) {
            mainView?.presentTrainArrived()
        } else {
            val runnable = Runnable {
                countdownJob(newSeconds)
            }
            handler.postDelayed(runnable, 1000)
        }
    }

    private fun sortList(stationName: String) {
        if(list.isNotEmpty()) {
            list = list.sortedBy {
                it.timeToStation
            }
            mainView?.presentInitialTime(calculateRemainingSeconds(list[0].expectedArrival), stationName)
        }
    }

    private fun calculateRemainingSeconds(expectedArrival: String): Int {
        val cal = DateUtil.convertToCalendar(expectedArrival)
        cal.add(Calendar.HOUR_OF_DAY, 1)
        val currentCal = Calendar.getInstance()
        return TimeUnit.MILLISECONDS.toSeconds(Math.abs(cal.timeInMillis - currentCal.timeInMillis)).toInt()
    }

    fun onDestroy() {
        mainView = null
        cd.dispose()
    }
}