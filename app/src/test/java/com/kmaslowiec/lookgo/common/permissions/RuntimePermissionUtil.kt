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

        val tested = permissionsState.toRuntimePermissionRequestState(false)

        assertEquals(PermissionState.AllGranted, tested)
    }

    @Test
    fun `returns NotAllGranted when some but not all permissions are granted`() {
        val permissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns false
            every { permissions.size } returns 2
            every { revokedPermissions.size } returns 1
        }

        val tested = permissionsState.toRuntimePermissionRequestState(false)

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

        val tested = permissionsState.toRuntimePermissionRequestState(false)

        assertEquals(PermissionState.BothDenied, tested)
    }

    @Test
    fun `returns FirstTime when all permissions are revoked and shouldShowRationale is false`() {
        val permissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns false
            every { permissions.size } returns 2
            every { revokedPermissions.size } returns 2
            every { shouldShowRationale } returns false
        }

        val tested = permissionsState.toRuntimePermissionRequestState(true)

        assertEquals(PermissionState.FirstTime, tested)
    }

    @Test
    fun `returns NeverAgain when all permissions are revoked and it is not first time`() {
        val permissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns false
            every { permissions.size } returns 2
            every { revokedPermissions.size } returns 2
            every { shouldShowRationale } returns false
        }

        val tested = permissionsState.toRuntimePermissionRequestState(false)

        assertEquals(PermissionState.NeverAgain, tested)
    }
}
