package com.drac.challenge.presentation.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.drac.challenge.common.*
import com.drac.challenge.data.impl.DataRepositoryImpl
import com.drac.challenge.data.network.MeliApi
import com.drac.challenge.data.network.model.ObjectItemsModel
import com.drac.challenge.domain.repository.DataRepository
import com.drac.challenge.domain.useCase.DetailItemUseCase
import com.drac.challenge.domain.useCase.SearchQueryUseCase
import com.drac.challenge.mapper.toDomain
import com.drac.challenge.presentation.common.State
import com.drac.challenge.presentation.ui.results.ResultVM
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
class DetailVMTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var meliApi: MeliApi

    private lateinit var dataRepository: DataRepository

    private lateinit var detailItemUseCase: DetailItemUseCase

    private lateinit var viewModel: DetailVM

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)

        dataRepository = DataRepositoryImpl(meliApi, testRule.dispatcher)
        detailItemUseCase = DetailItemUseCase(dataRepository)
        viewModel = DetailVM(
            detailItemUseCase,
            testRule.dispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun  `When Api Service returns a FullItem and desc Then evaluate the response`() = testRule.runBlockingTest {
        //Given
        coEvery { meliApi.getFullItem(any()) } returns itemFakeWithPictures
        coEvery { meliApi.getDescription(any()) } returns descriptionFake

        //When
        val results = mutableListOf<State<Unit>>()
        val job = launch(testRule.dispatcher) { viewModel.stateRequest.toList(results) }
        viewModel.executeRequest()

        advanceUntilIdle()

        //Then
        coVerify(exactly = 1) { meliApi.getFullItem(any()) }
        assertEquals(3, results.size)
        assert(results[0] is State.Empty)
        assert(results[1] is State.Loading)
        assert(results[2] is State.Success)

        val response = viewModel.fullItem.value
        assertEquals(response, itemFakeWithPicturesAndDesc_Domain)

        job.cancel()
    }

    @Test
    fun  `When Api Service FullItem returns an Exception Then get an error as response`() = testRule.runBlockingTest {
        //Given
        coEvery { meliApi.getFullItem(any()) }.throws(TimeoutException(""))
        coEvery { meliApi.getDescription(any()) } returns descriptionFake

        //When
        val results = mutableListOf<State<Unit>>()
        val job = launch(testRule.dispatcher) { viewModel.stateRequest.toList(results) }
        viewModel.executeRequest()

        advanceUntilIdle()

        //Then
        coVerify(exactly = 1) { meliApi.getFullItem(any()) }
        assertEquals(3, results.size)
        assert(results[0] is State.Empty)
        assert(results[1] is State.Loading)
        assert(results[2] is State.Error)
        job.cancel()
    }

    @Test
    fun  `When Api Service getDescription returns an Exception Then get the response without desc`() = testRule.runBlockingTest {
        //Given
        coEvery { meliApi.getFullItem(any()) } returns itemFakeWithPictures
        coEvery { meliApi.getDescription(any()) }.throws(TimeoutException(""))

        //When
        val results = mutableListOf<State<Unit>>()
        val job = launch(testRule.dispatcher) { viewModel.stateRequest.toList(results) }
        viewModel.executeRequest()

        advanceUntilIdle()

        //Then
        coVerify(exactly = 1) { meliApi.getFullItem(any()) }
        assertEquals(3, results.size)
        assert(results[0] is State.Empty)
        assert(results[1] is State.Loading)
        assert(results[2] is State.Success)

        val response = viewModel.fullItem.value
        assertEquals(response, itemFakeWithPictures_Domain)

        job.cancel()
    }

    @Test
    fun  `Verify the ItemId is being sent to the Request`() = testRule.runBlockingTest {
        //Given

        coEvery { meliApi.getFullItem(any()) } returns itemFakeWithPictures
        coEvery { meliApi.getDescription(any()) } returns descriptionFake

        //When
        val results = mutableListOf<State<Unit>>()
        val job = launch(testRule.dispatcher) { viewModel.stateRequest.toList(results) }

        viewModel.setId(fakeId)

        advanceUntilIdle()

        //Then
        coVerify(exactly = 1) { meliApi.getFullItem(fakeId) }
        coVerify(exactly = 1) { meliApi.getDescription(fakeId) }
        assertEquals(3, results.size)
        assert(results[0] is State.Empty)
        assert(results[1] is State.Loading)
        assert(results[2] is State.Success)

        val response = viewModel.fullItem.value
        assertEquals(response, itemFakeWithPicturesAndDesc_Domain)

        job.cancel()
    }



}