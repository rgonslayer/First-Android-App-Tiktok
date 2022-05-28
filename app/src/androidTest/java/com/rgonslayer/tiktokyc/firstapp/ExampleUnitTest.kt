package com.rgonslayer.tiktokyc.firstapp

import org.junit.Test

import org.junit.Assert.*
import com.rgonslayer.tiktokyc.*
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun getLocationUpdatesCoordinates() {

        val activity: MainActivity = MainActivity()
        activity.getLocation()
        assert(activity.currLong != 0.0 && activity.currLat !=0.0)
    }

}

