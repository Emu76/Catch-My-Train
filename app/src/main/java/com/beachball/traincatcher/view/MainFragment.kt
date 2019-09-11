package com.beachball.traincatcher.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beachball.traincatcher.R
import com.beachball.traincatcher.presenter.MainPresenter
import com.beachball.traincatcher.service.CountdownService
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment(), MainView {

    private lateinit var presenter: MainPresenter
    //private val viewModel: MainFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = MainPresenter(this,
                getString(R.string.tfl_app_id), getString(R.string.tfl_app_key))

        btn_canonbury.setOnClickListener {
            presenter.getArrivals(getString(R.string.station_code_canonbury), getString(R.string.destination_stratford))
        }

        btn_canary_wharf.setOnClickListener {
            presenter.getArrivals(getString(R.string.station_code_canary_wharf), getString(R.string.destination_stratford))
        }

        btn_stratford.setOnClickListener {
            presenter.getArrivals(getString(R.string.station_code_stratford), getString(R.string.destination_stratford_intl))
        }

        btn_stratford_intl.setOnClickListener {
            presenter.getArrivals(getString(R.string.station_code_stratford_intl), getString(R.string.destination_woolwich))
        }
    }

    override fun presentInitialTime(timeLeft: Int, stationName: String) {
        val minutes = timeLeft / 60
        main_text.text = String.format(getString(R.string.more_than) + " %d " + getString(R.string.minutes_remaining), minutes)
        presenter.startCountdownJob(timeLeft)
        val intent = Intent(context, CountdownService::class.java)
        intent.putExtra(CountdownService.TIME_LEFT, timeLeft)
        intent.putExtra(CountdownService.STATION_NAME, stationName)
        context?.startForegroundService(intent)
    }

    override fun presentNextMinute(timeLeft: Int) {
        val minutes = timeLeft / 60
        main_text.text = String.format("%d " + getString(R.string.minutes_remaining), minutes)
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