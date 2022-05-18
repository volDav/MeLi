package com.drac.challenge.presentation.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.FlowTurbine
import app.cash.turbine.test
import app.cash.turbine.testIn
import com.drac.challenge.common.CoroutineTestRule
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchVMTest {


    private lateinit var viewModel: SearchVM


    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Before
    fun onBefore() {
        viewModel = SearchVM()
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Verify SharedFlow returns data`() = coroutineTestRule.testDispatcher.runBlockingTest {

        viewModel.evaluateQuery()

        viewModel.badInput.collect {
            assertEquals(true, it)
        }



       /* val turbine = viewModel
            .badInput
            .testIn(this)


        viewModel.evaluateQuery()
*/

    }
}