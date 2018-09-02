package com.beachball.traincatcher.view

import android.content.Context
import android.os.*
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.beachball.traincatcher.presenter.MainPresenter
import com.beachball.traincatcher.R
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment(), MainView {

    private val presenter: MainPresenter = MainPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getArrivals()
    }

    override fun presentNextArrival(timeLeft: Int) {
        val minutes = timeLeft / 60
        main_text.text = String.format("%d", minutes)
        val handler = Handler()
        val specialRunnable = Runnable {
            specialVibrationJob()
        }
        val runnable = Runnable {
            vibrationJob()
        }
        if(minutes == 0) {
            for (i in 1..5) {
                val timer: Number = 250 * i
                handler.postDelayed(specialRunnable, timer.toLong())
            }
        } else {
            for (i in 1..minutes) {
                val timer: Number = 1000 * i
                handler.postDelayed(runnable, timer.toLong())
            }
        }
    }

    private fun specialVibrationJob() {
        val vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(250)
        }
    }

    private fun vibrationJob() {
        val vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(750, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(750)
        }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}