package com.beachball.traincatcher.view

interface MainView {

    fun presentNextArrival(timeLeftStr: String)

    fun presentSecondsLeft(timeLeft: Int)
}