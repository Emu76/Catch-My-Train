package com.beachball.traincatcher

import com.beachball.traincatcher.model.Arrival
import com.beachball.traincatcher.presenter.MainPresenter
import com.beachball.traincatcher.view.MainView
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock

import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Mock
    lateinit var mockView: MainView

    @Mock
    lateinit var mockArrival: Arrival

    @Test
    fun sortListTest() {
        //Given
        val testPresenter = MainPresenter(mockView, "", "")
        `when`(mockArrival.expectedArrival).thenReturn(ARRIVAL_TIME)
        val mockList = listOf(mockArrival)
        testPresenter.list = mockList

        //When
        testPresenter.sortList(STATION_NAME)

        //Then
        verify(mockView).presentInitialTime(ArgumentMatchers.anyInt(),
                ArgumentMatchers.matches(STATION_NAME))
    }

    companion object {
        const val STATION_NAME = "Stratford"
        const val ARRIVAL_TIME = "2019-12-01T15:00:00Z"
    }
}
