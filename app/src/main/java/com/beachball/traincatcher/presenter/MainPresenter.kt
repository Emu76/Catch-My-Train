package com.beachball.traincatcher.presenter

import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import com.beachball.traincatcher.interactor.ArrivalInteractor
import com.beachball.traincatcher.model.Arrival
import com.beachball.traincatcher.util.DateUtil
import com.beachball.traincatcher.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class MainPresenter(private var mainView: MainView?) {

    companion object {
        private const val LONG_BUZZ: Long = 750
        private const val SHORT_BUZZ: Long = 250
        private const val WAIT_TIME: Long = 500
        private const val QUICK_BUZZ_NUM = 5
        private const val TEST_STATION_ID = "940GZZDLSIT"
    }

    private val interactor: ArrivalInteractor = ArrivalInteractor()
    private val list: MutableList<Arrival> = ArrayList()

    fun getArrivals() {
        interactor.getArrivalsByStationId(TEST_STATION_ID)
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

    fun startJob(minutes: Int, vibrator: Vibrator?) {
        val handler = Handler()
        val specialRunnable = Runnable {
            specialVibrationJob(vibrator)
        }
        val runnable = Runnable {
            vibrationJob(vibrator)
        }
        if(minutes == 0) {
            for (i in 1..QUICK_BUZZ_NUM) {
                val timer: Number = (SHORT_BUZZ + WAIT_TIME) * i
                handler.postDelayed(specialRunnable, timer.toLong())
            }
        } else {
            for (i in 1..minutes) {
                val timer: Number = (LONG_BUZZ + WAIT_TIME) * i
                handler.postDelayed(runnable, timer.toLong())
            }
        }
    }

    private fun specialVibrationJob(vibrator: Vibrator?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(SHORT_BUZZ, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(SHORT_BUZZ)
        }
    }

    private fun vibrationJob(vibrator: Vibrator?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(LONG_BUZZ, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(LONG_BUZZ)
        }
    }

    private fun sortList() {
        if(list.size > 0) {
            list.sortBy {
                it.timeToStation
            }
            mainView?.presentNextArrival(calculateRemainingMinutes(list[0].expectedArrival))
        }
    }

    private fun calculateRemainingMinutes(expectedArrival: String): Int {
        val cal = DateUtil.convertToCalendar(expectedArrival)
        cal.add(Calendar.HOUR_OF_DAY, 1)
        val currentCal = Calendar.getInstance()
        return TimeUnit.MILLISECONDS.toSeconds(Math.abs(cal.timeInMillis - currentCal.timeInMillis)).toInt()
    }

    fun onDestroy() {
        mainView = null
    }
}