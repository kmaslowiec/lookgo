package com.kmaslowiec.lookgo.mainviewmodel

import com.kmaslowiec.lookgo.CoroutinesUnconfinedDispatcherTestExtension
import com.kmaslowiec.lookgo.main.viewmodel.MainScreenViewModel
import com.kmaslowiec.lookgo.preferences.AppPreferencesRepository
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

    @Test
    fun `test isFirstTime emits correct value`() = runTest {
        assertTrue(tested.isFirstTime.first())
    }

    @Test
    fun `test firstTimeAccess calls repository`() = runTest {
        tested.firstTimeAccess()

        coVerify(exactly = 1) { preferencesRepository.firstTimeAccess() }
    }
}
