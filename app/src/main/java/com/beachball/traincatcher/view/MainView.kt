package com.beachball.traincatcher.view

interface MainView {

    fun presentNextArrival(timeLeft: Int)

    fun presentTrainArrived()
}