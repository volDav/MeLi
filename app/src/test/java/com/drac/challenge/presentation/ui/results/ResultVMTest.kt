package com.drac.challenge.presentation.ui.results

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.drac.challenge.common.*
import com.drac.challenge.common.Variables.limit
import com.drac.challenge.common.Variables.limitValue
import com.drac.challenge.common.Variables.mco
import com.drac.challenge.common.Variables.offset
import com.drac.challenge.common.Variables.q
import com.drac.challenge.data.impl.DataRepositoryImpl
import com.drac.challenge.data.network.MeliApi
import com.drac.challenge.data.network.model.ObjectItemsModel
import com.drac.challenge.domain.repository.DataRepository
import com.drac.challenge.domain.useCase.SearchQueryUseCase
import com.drac.challenge.mapper.toDomain
import com.drac.challenge.presentation.common.State
import io.mockk.*
import io.mockk.impl.annotations.MockK
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
class ResultVMTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var meliApi: MeliApi

    private lateinit var dataRepository: DataRepository

    private lateinit var searchQueryUseCase: SearchQueryUseCase

    private lateinit var viewModel: ResultVM


    @Before
    fun onBefore() {
        MockKAnnotations.init(this)

        dataRepository = DataRepositoryImpl(meliApi, testRule.dispatcher)
        searchQueryUseCase = SearchQueryUseCase(dataRepository)
        viewModel = ResultVM(
            searchQueryUseCase,
            testRule.dispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun  `When Api Service returns 0 items then get an Exception`() = testRule.runBlockingTest {
        //Given
        val objectModel = ObjectItemsModel(listOf())
        coEvery { meliApi.getItems(any(), any()) } returns objectModel

        //When
        val results = mutableListOf<State<Boolean>>()
        val job = launch(testRule.dispatcher) { viewModel.stateRequest.toList(results) }

        viewModel.executeQuery()

        advanceUntilIdle()

        //Then
        assertEquals(3, results.size)
        assert(results[0] is State.Empty)
        assert(results[1] is State.Loading)
        assert(results[2] is State.Error)
        job.cancel()

    }

    //TODO falta el Unit test por categoria

    @Test
    fun  `Verify id MCO and HashMap objects are being sent to the request`() = testRule.runBlockingTest {
        //Given
        val query = "Xiaomi"

        val objectModel = ObjectItemsModel(listOf())
        coEvery { meliApi.getItems(any(), any()) } returns objectModel

        //When
        val results = mutableListOf<State<Boolean>>()
        val job = launch(testRule.dispatcher) { viewModel.stateRequest.toList(results) }

        viewModel.setQueryData(query)

        advanceUntilIdle()

        //Then
        val siteId = mco
        val map = hashMapOf(
            q to query,
            limit to limitValue,
            offset to "0"
        )
        val slotMco = slot<String>()
        val slotMap = slot<HashMap<String,String>>()
        coVerify(exactly = 1) { meliApi.getItems(capture(slotMco), capture(slotMap)) }

        assert(slotMco.captured == siteId)
        assert(slotMap.captured == map)

        job.cancel()
    }



    @Test
    fun `When the Api is called and returns data Then validate the answer is OK`() = testRule.runBlockingTest {
        //Given
        val list = listOf(itemFakeWithPictures)
        val objectResponse = ObjectItemsModel(results = list)
        coEvery { meliApi.getItems(any(), any()) } returns objectResponse


        //When
        val results = mutableListOf<State<Boolean>>()
        val job = launch(testRule.dispatcher) { viewModel.stateRequest.toList(results) }

        viewModel.executeQuery()

        advanceUntilIdle()

        //Then
        coVerify(exactly = 1) { meliApi.getItems(any(), any()) }
        assertEquals(3, results.size)
        assert(results[0] is State.Empty)
        assert(results[1] is State.Loading)
        assert(results[2] is State.Success)
        assert(viewModel.loadRecycler.value?.first() == list.first().toDomain())
        job.cancel()

    }


    @Test
    fun `When Api Return a Exception then get error`() = testRule.runBlockingTest {
        //Given
        coEvery { meliApi.getItems(any(), any()) }.throws(TimeoutException(""))

        //When
        val results = mutableListOf<State<Boolean>>()
        val job = launch(testRule.dispatcher) { viewModel.stateRequest.toList(results) }

        viewModel.executeQuery()

        advanceUntilIdle()

        //Then
        coVerify(exactly = 1) { meliApi.getItems(any(), any()) }
        assertEquals(3, results.size)
        assert(results[0] is State.Empty)
        assert(results[1] is State.Loading)
        assert(results[2] is State.Error)
        job.cancel()
    }



}