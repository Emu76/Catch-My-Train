package com.beachball.traincatcher.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.beachball.traincatcher.R
import com.beachball.traincatcher.view.MainFragment

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit()
    }
}
