package com.beachball.traincatcher.view

interface MainView {

    fun presentInitialTime(timeLeft: Int)

    fun presentNextMinute(timeLeft: Int)

    fun presentTrainArrived()

    fun presentLoading()
}