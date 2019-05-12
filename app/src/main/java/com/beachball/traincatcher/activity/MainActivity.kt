package com.beachball.traincatcher.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.beachball.traincatcher.R
import com.beachball.traincatcher.view.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit()
    }
}
