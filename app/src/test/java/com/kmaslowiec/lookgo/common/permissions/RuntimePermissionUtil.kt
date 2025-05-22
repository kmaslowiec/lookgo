package com.kmaslowiec.lookgo.common.permissions

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.kmaslowiec.lookgo.permissions.states.PermissionState
import com.kmaslowiec.lookgo.permissions.toRuntimePermissionRequestState
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalPermissionsApi::class)
class RuntimePermissionUtilTest {

    @Test
    fun `returns AllGranted when all permissions are granted`() {
        val permissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns true
        }

        val tested = permissionsState.toRuntimePermissionRequestState()

        assertEquals(PermissionState.AllGranted, tested)
    }

    @Test
    fun `returns NotAllGranted when some but not all permissions are granted`() {
        val permissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns false
            every { permissions.size } returns 2
            every { revokedPermissions.size } returns 1
        }

        val tested = permissionsState.toRuntimePermissionRequestState()

        assertEquals(PermissionState.NotAllGranted, tested)
    }

    @Test
    fun `returns BothDenied when all permissions are revoked and shouldShowRationale is true`() {
        val permissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns false
            every { permissions.size } returns 2
            every { revokedPermissions.size } returns 2
            every { shouldShowRationale } returns true
        }

        val tested = permissionsState.toRuntimePermissionRequestState()

        assertEquals(PermissionState.BothDenied, tested)
    }

    @Test
    fun `returns FirstTimeAndNeverAgain when all permissions are revoked and shouldShowRationale is false`() {
        val permissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns false
            every { permissions.size } returns 2
            every { revokedPermissions.size } returns 2
            every { shouldShowRationale } returns false
        }

        val tested = permissionsState.toRuntimePermissionRequestState()

        assertEquals(PermissionState.FirstTimeOrNeverAgain, tested)
    }
}
