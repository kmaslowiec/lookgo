package com.kmaslowiec.lookgo.common.permissions

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.kmaslowiec.lookgo.main.permissions.getRuntimePermissionRequestState
import com.kmaslowiec.lookgo.main.permissions.states.LocationPermissionState
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalPermissionsApi::class)
class RuntimePermissionHelperKtTest {

    @Test
    fun `returns AllGranted when all permissions are granted`() {
        val mockPermissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns true
        }

        val result = getRuntimePermissionRequestState(mockPermissionsState)

        assertEquals(LocationPermissionState.AllGranted, result)
    }

    @Test
    fun `returns NotAllGranted when some but not all permissions are granted`() {
        val mockPermissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns false
            every { permissions.size } returns 2
            every { revokedPermissions.size } returns 1
        }

        val result = getRuntimePermissionRequestState(mockPermissionsState)

        assertEquals(LocationPermissionState.NotAllGranted, result)
    }

    @Test
    fun `returns BothDenied when all permissions are revoked and shouldShowRationale is true`() {
        val mockPermissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns false
            every { permissions.size } returns 2
            every { revokedPermissions.size } returns 2
            every { shouldShowRationale } returns true
        }

        val result = getRuntimePermissionRequestState(mockPermissionsState)

        assertEquals(LocationPermissionState.BothDenied, result)
    }

    @Test
    fun `returns FirstTimeAndNeverAgain when all permissions are revoked and shouldShowRationale is false`() {
        val mockPermissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns false
            every { permissions.size } returns 2
            every { revokedPermissions.size } returns 2
            every { shouldShowRationale } returns false
        }

        val result = getRuntimePermissionRequestState(mockPermissionsState)

        assertEquals(LocationPermissionState.FirstTimeAndNeverAgain, result)
    }
}
