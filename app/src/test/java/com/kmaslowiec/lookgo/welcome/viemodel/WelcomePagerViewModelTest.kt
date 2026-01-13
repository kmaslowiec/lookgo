package com.kmaslowiec.lookgo.welcome.viemodel

import app.cash.turbine.test
import com.kmaslowiec.lookgo.welcome.uievent.WelcomePagerUIEvent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WelcomePagerViewModelTest {

    @Test
    fun `onPermissionActionClick should emit NavigateToLocationPermissionScreen event`() = runTest {
        val viewModel = WelcomePagerViewModel()

        viewModel.uiEvent.test {
            viewModel.onPermissionActionClick()
            assertEquals(WelcomePagerUIEvent.NavigateToLocationPermissionScreen, awaitItem())
        }
    }
}
