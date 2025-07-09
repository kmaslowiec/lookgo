package com.kmaslowiec.lookgo.mainviewmodel

import com.kmaslowiec.lookgo.CoroutinesUnconfinedDispatcherTestExtension
import com.kmaslowiec.lookgo.main.viewmodel.MainScreenViewModel
import com.kmaslowiec.lookgo.preferences.AppPreferencesRepository
import com.kmaslowiec.lookgo.preferences.state.PreferencesState
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesUnconfinedDispatcherTestExtension::class)
class MainScreenViewModelTest {

    private var preferencesRepository: AppPreferencesRepository = mockk(relaxed = true) {
        every { isFirstTime } returns flowOf(true)
    }
    private val tested: MainScreenViewModel = MainScreenViewModel(preferencesRepository)

    //TODO test for State Loading

    @Test
    fun `preference is loaded successfully `() = runTest {
        assertTrue(tested.preferencesState.first() is PreferencesState.Success)
    }

    @Test
    fun `firstTimeAccess calls repository`() = runTest {
        tested.firstTimeAccess()

        coVerify(exactly = 1) { preferencesRepository.firstTimeAccess() }
    }
}
