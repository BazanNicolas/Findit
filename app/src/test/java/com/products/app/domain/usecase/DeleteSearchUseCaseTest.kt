package com.products.app.domain.usecase

import com.products.app.domain.model.SearchHistory
import com.products.app.domain.repository.SearchHistoryRepository
import com.products.app.util.MockDataFactory
import com.products.app.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class DeleteSearchUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var searchHistoryRepository: SearchHistoryRepository

    private lateinit var useCase: DeleteSearchUseCase

    @Before
    fun setUp() {
        useCase = DeleteSearchUseCase(searchHistoryRepository)
    }

    @Test
    fun `invoke should call repository deleteSearch with correct search`() = runTest {

        val search = MockDataFactory.createSearchHistory("test search")
        useCase(search)
        verify(searchHistoryRepository).deleteSearch(search)
    }

    @Test
    fun `invoke with search containing special characters should call repository`() = runTest {

        val search = MockDataFactory.createSearchHistory("search with special chars: @#$%^&*()")
        useCase(search)
        verify(searchHistoryRepository).deleteSearch(search)
    }

    @Test
    fun `invoke with search containing empty query should call repository`() = runTest {

        val search = MockDataFactory.createSearchHistory("")
        useCase(search)
        verify(searchHistoryRepository).deleteSearch(search)
    }

    @Test
    fun `invoke with search containing very long query should call repository`() = runTest {

        val longQuery = "This is a very long search query that might exceed normal limits and should be handled correctly by the use case"
        val search = MockDataFactory.createSearchHistory(longQuery)
        useCase(search)
        verify(searchHistoryRepository).deleteSearch(search)
    }

    @Test
    fun `invoke with search with zero timestamp should call repository`() = runTest {

        val search = SearchHistory("test query", 0L)
        useCase(search)
        verify(searchHistoryRepository).deleteSearch(search)
    }

    @Test
    fun `invoke with search with negative timestamp should call repository`() = runTest {

        val search = SearchHistory("test query", -1000L)
        useCase(search)
        verify(searchHistoryRepository).deleteSearch(search)
    }

    @Test
    fun `multiple invocations with different searches should call repository multiple times`() = runTest {

        val search1 = MockDataFactory.createSearchHistory("search 1")
        val search2 = MockDataFactory.createSearchHistory("search 2")
        useCase(search1)
        useCase(search2)
        verify(searchHistoryRepository).deleteSearch(search1)
        verify(searchHistoryRepository).deleteSearch(search2)
    }

}
