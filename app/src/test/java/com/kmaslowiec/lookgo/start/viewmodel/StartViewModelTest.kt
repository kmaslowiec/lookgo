package com.kmaslowiec.lookgo.start.viewmodel

import app.cash.turbine.test
import com.kmaslowiec.lookgo.start.uievent.StartUIEvent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StartViewModelTest {

    @Test
    fun `onPermissionAllGranted should emit NavigateToMainScreen event`() = runTest {
        val viewModel = StartViewModel()

        viewModel.uiEvent.test {
            viewModel.onPermissionAllGranted()
            assertEquals(StartUIEvent.NavigateToMainScreen, awaitItem())
        }
    }

    @Test
    fun `onFirstTime should emit NavigateToNavigateToWelcome event`() = runTest {
        val viewModel = StartViewModel()

        viewModel.uiEvent.test {
            viewModel.onFirstTime()
            assertEquals(StartUIEvent.NavigateToNavigateToWelcome, awaitItem())
        }
    }

    @Test
    fun `onOtherPermissions should emit NavigateToNavigateToLocationPermission event`() = runTest {
        val viewModel = StartViewModel()

        viewModel.uiEvent.test {
            viewModel.onOtherPermissions()
            assertEquals(StartUIEvent.NavigateToNavigateToLocationPermission, awaitItem())
        }
    }
}
