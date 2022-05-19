package com.drac.challenge.presentation.ui.search

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.drac.challenge.common.CoroutineTestRule
import io.mockk.unmockkAll
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchVMTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    private lateinit var viewModel: SearchVM

    @Before
    fun onBefore() {
        viewModel = SearchVM(
            SavedStateHandle(),
            testRule.dispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Verify SharedFlow badInput returns true`() = testRule.runBlockingTest {
        viewModel.input.set("")

        val job = launch(testRule.dispatcher) {
            viewModel.badInput.test {
                assertEquals(true, awaitItem())
                cancelAndConsumeRemainingEvents()

            }
            viewModel.goToSearch.test {
                expectNoEvents()
            }
        }
        viewModel.evaluateQuery()
        job.join()
        job.cancel()
    }

    @Test
    fun `Verify SharedFlow goToSearch returns the Query`() = testRule.runBlockingTest {

        val query = "Xiaomi"
        viewModel.input.set(query)

        val job = launch(testRule.dispatcher) {
            viewModel.goToSearch.test {
                assertEquals(query, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
            viewModel.badInput.test {
                expectNoEvents()
            }
        }
        viewModel.evaluateQuery()
        job.join()
        job.cancel()
    }




}