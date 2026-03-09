package com.kmaslowiec.lookgo.mainviewmodel

import com.kmaslowiec.lookgo.preferences.repository.AppPreferencesRepository
import com.kmaslowiec.lookgo.preferences.state.PreferencesState
import com.kmaslowiec.lookgo.preferences.viewmodel.PreferencesViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PreferencesViewModelTest {

    private var preferencesRepository: AppPreferencesRepository = mockk(relaxed = true) {
        every { isFirstTime } returns flowOf(true)
    }
    private val tested: PreferencesViewModel = PreferencesViewModel(preferencesRepository)

    @Test
    fun `preference is loading `() = runTest {
        every { preferencesRepository.isFirstTime } returns emptyFlow()

        val tested = PreferencesViewModel(preferencesRepository)

        assertTrue(tested.preferencesState.value is PreferencesState.Loading)
    }

    @Test
    fun `preference is loaded successfully `() = runTest {
        assertTrue(tested.preferencesState.value is PreferencesState.Success)
    }

    @Test
    fun `firstTimeAccess calls repository`() = runTest {
        tested.firstTimeAccess()

        coVerify(exactly = 1) { preferencesRepository.firstTimeAccess() }
    }
}
