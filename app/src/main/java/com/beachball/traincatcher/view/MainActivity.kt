package com.beachball.traincatcher.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.beachball.traincatcher.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit()
    }
}
