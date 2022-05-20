package com.drac.challenge.domain.useCase

import com.drac.challenge.common.CoroutineTestRule
import com.drac.challenge.common.ResultOrError
import com.drac.challenge.common.Variables.limit
import com.drac.challenge.common.Variables.mco
import com.drac.challenge.common.Variables.offset
import com.drac.challenge.common.Variables.q
import com.drac.challenge.data.impl.DataRepositoryImpl
import com.drac.challenge.data.network.model.ObjectItemsModel
import com.drac.challenge.data.network.MeliApi
import com.drac.challenge.common.itemFakeWithoutPictures
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
class SearchQueryUseCaseTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var meliApi: MeliApi

    private lateinit var dataRepository: DataRepository

    lateinit var searchQueryUseCase: SearchQueryUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        dataRepository = DataRepositoryImpl(meliApi,testRule.dispatcher)
        searchQueryUseCase = SearchQueryUseCase(dataRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When Api Returns something then get that values`() = testRule.runBlockingTest {
        //Given
        val list = listOf(itemFakeWithoutPictures)
        val objectResponse = ObjectItemsModel(results = list)
        coEvery { meliApi.getItems("", hashMapOf()) } returns objectResponse

        //When
        val response = searchQueryUseCase.getResults("", hashMapOf())

        //Then
        coVerify { meliApi.getItems("", hashMapOf())  }
        assert((response is ResultOrError.Result))
        val listDomain = list.map { it.toDomain() }
        assert((response as? ResultOrError.Result)?.p0 == listDomain)

    }


    @Test
    fun `When Api Returns nothing then get NullPointerException`() = testRule.runBlockingTest {
        //Given
        val objectResponse = ObjectItemsModel(results = listOf())
        coEvery { meliApi.getItems("", hashMapOf()) } returns objectResponse

        //When
        val response = searchQueryUseCase.getResults("", hashMapOf())

        //Then
        coVerify { meliApi.getItems("", hashMapOf())  }
        assert((response is ResultOrError.Fail))
        assert((response as? ResultOrError.Fail)?.p0 is NullPointerException)

    }


    @Test
    fun `When Api Return a Exception then get that exception`() = testRule.runBlockingTest {
        //Given
        coEvery { meliApi.getItems("", hashMapOf()) }.throws(TimeoutException(""))

        //When
        val response = searchQueryUseCase.getResults("", hashMapOf())

        //Then
        coVerify { meliApi.getItems("", hashMapOf())  }
        assert(response is ResultOrError.Fail)
        assert((response as? ResultOrError.Fail)?.p0 is TimeoutException)

    }

    @Test
    fun `Verify id MCO and HashMap object is being sent to the request`() = testRule.runBlockingTest {
        //When
        val map = hashMapOf(
            q to "Headphones",
            limit to "15",
            offset to "0"
        )
        searchQueryUseCase.getResults(mco, map)

        val slotMCO = slot<String>()
        val slotMap = slot<HashMap<String, String>>()

        //Verify
        coVerify(exactly = 1) { meliApi.getItems(capture(slotMCO), capture(slotMap))   }
        assert(slotMCO.captured == mco)
        assert(slotMap.captured == map)
    }

}