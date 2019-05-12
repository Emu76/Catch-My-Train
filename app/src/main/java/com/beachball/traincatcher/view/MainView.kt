package com.beachball.traincatcher.view

interface MainView {

    fun presentInitialTime(timeLeft: Int, stationName: String)

    fun presentNextMinute(timeLeft: Int)

    fun presentTrainArrived()

    fun presentLoading()
}