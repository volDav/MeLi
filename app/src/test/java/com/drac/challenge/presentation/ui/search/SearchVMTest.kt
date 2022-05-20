package com.drac.challenge.presentation.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.drac.challenge.common.CoroutineTestRule
import com.drac.challenge.common.Variables.mco
import com.drac.challenge.common.listCategoriesModelFake
import com.drac.challenge.data.impl.DataRepositoryImpl
import com.drac.challenge.data.network.MeliApi
import com.drac.challenge.data.network.model.CategoryModel
import com.drac.challenge.domain.repository.DataRepository
import com.drac.challenge.domain.useCase.SearchQueryUseCase
import com.drac.challenge.mapper.toDomain
import com.drac.challenge.presentation.common.State
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchVMTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var meliApi: MeliApi

    private lateinit var dataRepository: DataRepository

    private lateinit var searchQueryUseCase: SearchQueryUseCase

    private lateinit var viewModel: SearchVM

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        dataRepository = DataRepositoryImpl(meliApi,testRule.dispatcher)
        searchQueryUseCase = SearchQueryUseCase(dataRepository)
        viewModel = SearchVM(
            SavedStateHandle(),
            searchQueryUseCase,
            testRule.dispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Verify SharedFlow badInput returns true When I send an empty input`() = testRule.runBlockingTest {
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
    fun `Verify goToSearch returns the Query When evaluateQuery function is called`() = testRule.runBlockingTest {

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

    @Test
    fun `When getCategories function is called Then validate the api is called and the answer is correct`() = testRule.runBlockingTest {
        //Given
        coEvery { meliApi.getCategories(any()) } returns listCategoriesModelFake

        //When
        val job = launch(testRule.dispatcher) {
            viewModel.stateRequest.test {
                assert(awaitItem() is State.Empty)
                assert(awaitItem() is State.Loading)
                assert(awaitItem() is State.Success)
                cancelAndConsumeRemainingEvents()
            }
        }

        viewModel.getCategories()

        advanceUntilIdle()

        //Then
        coVerify(exactly = 1) { meliApi.getCategories(any())  }
        assert(viewModel.loadRecycler.value == listCategoriesModelFake.map { it.toDomain() })

        job.cancel()
    }

    @Test
    fun `When the api answered empty list Then get an error`() = testRule.runBlockingTest {
        //Given
        coEvery { meliApi.getCategories(any()) } returns listOf()

        //When
        val job = launch(testRule.dispatcher) {
            viewModel.stateRequest.test {
                assert(awaitItem() is State.Empty)
                assert(awaitItem() is State.Loading)
                assert(awaitItem() is State.Error)
                cancelAndConsumeRemainingEvents()
            }
        }

        viewModel.getCategories()

        advanceUntilIdle()

        //Then

        job.cancel()
    }

    @Test
    fun `When getCategories function is called Then validate the parameter of the service is correct`() = testRule.runBlockingTest{
        //Given
        coEvery { meliApi.getCategories(any()) } returns listCategoriesModelFake

        //When
        viewModel.getCategories()

        advanceUntilIdle()

        //Then
        val slot = slot<String>()
        coVerify(exactly = 1) { meliApi.getCategories(capture(slot))  }
        assert(slot.captured == mco)
    }




}