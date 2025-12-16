package com.kmaslowiec.lookgo.permissions.viewmodel

import app.cash.turbine.test
import com.kmaslowiec.lookgo.permissions.uievent.LocationPermissionUIEvent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LocationPermissionViewModelTest {

    @Test
    fun `onPermissionAllGranted should emit NavigateToMainScreen event`() = runTest {
        val viewModel = LocationPermissionViewModel()

        viewModel.uiEvent.test {
            viewModel.onPermissionAllGranted()
            assertEquals(LocationPermissionUIEvent.NavigateToMainScreen, awaitItem())
        }
    }

    @Test
    fun `showDialogState should be true when the dialog is shown`() {
        val viewModel = LocationPermissionViewModel()

        viewModel.showDialog()

        assertEquals(true, viewModel.dialogState.value)
    }

    @Test
    fun `showDialogState should be false when the dialog is dismissed`() {
        val viewModel = LocationPermissionViewModel()

        viewModel.dismissDialog()

        assertEquals(false, viewModel.dialogState.value)
    }
}
