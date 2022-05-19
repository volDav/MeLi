package com.drac.challenge.domain.useCase

import com.drac.challenge.common.*
import com.drac.challenge.data.impl.DataRepositoryImpl
import com.drac.challenge.data.network.MeliApi
import com.drac.challenge.domain.repository.DataRepository
import com.drac.challenge.mapper.toDomain
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.NullPointerException
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
class DetailItemUseCaseTest {


    @get:Rule
    val testRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var meliApi: MeliApi

    private lateinit var dataRepository: DataRepository

    private lateinit var detailItemUseCase: DetailItemUseCase


    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        dataRepository = DataRepositoryImpl(meliApi, testRule.dispatcher)
        detailItemUseCase = DetailItemUseCase(dataRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When Api Returns something then get that values`() = testRule.runBlockingTest {
        //Given
        coEvery { meliApi.getFullItem("") } returns itemFakeWithPictures

        //When
        val response = detailItemUseCase.getDetailItem("")

        //Then
        assert(response is ResultOrError.Result)
        assert((response as? ResultOrError.Result)?.p0 == itemFakeWithPictures.toDomain())
    }


    @Test
    fun `When Api returns an item and if it doesn't contains pictures then return an exception`() = testRule.runBlockingTest {
        //Given
        coEvery { meliApi.getFullItem("") } returns itemFakeWithoutPictures

        //When
        val response = detailItemUseCase.getDetailItem("")

        //Then
        assert((response is ResultOrError.Fail))
        assert((response as? ResultOrError.Fail)?.p0 is NullPointerException)
    }


    @Test
    fun `When Api Return a Exception then get that exception from Detail`() = testRule.runBlockingTest {
        //Given
        coEvery { meliApi.getFullItem("") }.throws(TimeoutException(""))

        //When
        val response = detailItemUseCase.getDetailItem("")

        //Then
        assert(response is ResultOrError.Fail)
        assert((response as? ResultOrError.Fail)?.p0 is TimeoutException)
    }

    @Test
    fun `Verify itemID is being sent to the request from Detail`() = testRule.runBlockingTest {
        //Given
        val itemID = "MCO658984720"

        //When
        detailItemUseCase.getDetailItem(itemID)
        val slotItemId = slot<String>()

        //Then
        coVerify(exactly = 1) { meliApi.getFullItem(capture(slotItemId))   }
        assert(slotItemId.captured == itemID)
    }


    @Test
    fun `When Api returns something Then get that values` () = testRule.runBlockingTest {
        //Given
        coEvery { meliApi.getDescription("") } returns descriptionFake

        //When
        val response = detailItemUseCase.getDescription("")

        //Then
        assert(response is ResultOrError.Result)
        val modelDescription = descriptionFake.toDomain()
        assert((response as? ResultOrError.Result)?.p0 == modelDescription)


    }

    @Test
    fun `When Api Return a Exception then get that exception from Description`() = testRule.runBlockingTest {
        //Given
        coEvery { meliApi.getDescription("") }.throws(TimeoutException(""))

        //When
        val response = detailItemUseCase.getDescription("")

        //Then
        assert(response is ResultOrError.Fail)
        assert((response as? ResultOrError.Fail)?.p0 is TimeoutException)
    }


    @Test
    fun `Verify itemID is being sent to the request from Description`() = testRule.runBlockingTest {
        //Given
        val itemID = "MCO658984720"

        //When
        detailItemUseCase.getDescription(itemID)
        val slotItemId = slot<String>()

        //Then
        coVerify(exactly = 1) { meliApi.getDescription(capture(slotItemId))   }
        assert(slotItemId.captured == itemID)
    }




}