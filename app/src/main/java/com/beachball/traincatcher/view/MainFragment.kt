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

    override fun presentNextArrival(timeLeftStr: String) {
        main_text.text = timeLeftStr
        val vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        presenter.startJob(Integer.parseInt(timeLeftStr), vibrator)
        presenter.startCountdownJob(Integer.parseInt(timeLeftStr), vibrator)
    }

    override fun presentSecondsLeft(timeLeft: Int) {
        if(timeLeft == 0) {
            main_text.text = getString(R.string.arrival)
        } else {
            main_text.text = String.format("%d", timeLeft)
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