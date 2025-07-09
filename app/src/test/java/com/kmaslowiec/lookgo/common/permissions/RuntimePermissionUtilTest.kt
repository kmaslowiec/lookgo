package com.kmaslowiec.lookgo.common.permissions

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.kmaslowiec.lookgo.permissions.state.PermissionState
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

        val result = permissionsState.toRuntimePermissionRequestState(false)

        assertEquals(PermissionState.AllGranted, result)
    }

    @Test
    fun `returns NotAllGranted when some but not all permissions are granted`() {
        val permissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns false
            every { permissions.size } returns 2
            every { revokedPermissions.size } returns 1
        }

        val result = permissionsState.toRuntimePermissionRequestState(false)

        assertEquals(PermissionState.NotAllGranted, result)
    }

    @Test
    fun `returns BothDenied when all permissions are revoked and shouldShowRationale is true`() {
        val permissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns false
            every { permissions.size } returns 2
            every { revokedPermissions.size } returns 2
            every { shouldShowRationale } returns true
        }

        val result = permissionsState.toRuntimePermissionRequestState(false)

        assertEquals(PermissionState.BothDenied, result)
    }

    @Test
    fun `returns FirstTime when isFirstTime is true`() {
        val permissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns false
            every { permissions.size } returns 2
            every { revokedPermissions.size } returns 2
            every { shouldShowRationale } returns false
        }

        val result = permissionsState.toRuntimePermissionRequestState(true)

        assertEquals(PermissionState.FirstTime, result)
    }

    @Test
    fun `returns NeverAgain when isFirstTime is false and other conditions are not fullfilled`() {
        val permissionsState = mockk<MultiplePermissionsState> {
            every { allPermissionsGranted } returns false
            every { permissions.size } returns 2
            every { revokedPermissions.size } returns 2
            every { shouldShowRationale } returns false
        }

        val result = permissionsState.toRuntimePermissionRequestState(true)

        assertEquals(PermissionState.FirstTime, result)
    }
}
