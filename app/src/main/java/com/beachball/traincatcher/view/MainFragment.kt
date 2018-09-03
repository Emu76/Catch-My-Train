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

        btn_canonbury.setOnClickListener {
            presenter.getArrivals("910GCNNB")
        }

        btn_canary_wharf.setOnClickListener {
            presenter.getArrivals("9400ZZDLCAN1")
        }

        btn_stratford.setOnClickListener {
            presenter.getArrivals("9400ZZDLSTD1")
        }

        btn_stratford_intl.setOnClickListener {
            presenter.getArrivals("940GZZDLSIT")
        }
    }

    override fun presentInitialTime(timeLeft: Int) {
        val vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        presentTimeAndVibrate(timeLeft)
        presenter.startCountdownJob(timeLeft, vibrator)
    }

    override fun presentNextMinute(timeLeft: Int) {
        presentTimeAndVibrate(timeLeft)
    }

    private fun presentTimeAndVibrate(timeLeft: Int) {
        val minutes = timeLeft / 60
        main_text.text = String.format(getString(R.string.more_than) + " %d " + getString(R.string.minutes_remaining), minutes)
        val vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        presenter.startVibrationJob(minutes, vibrator)
    }

    override fun presentTrainArrived() {
        main_text.text = getString(R.string.arrival)
    }

    override fun presentLoading() {
        main_text.text = getString(R.string.loading)
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