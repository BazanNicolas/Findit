package com.products.app.domain.usecase

import com.products.app.domain.model.SearchHistory
import com.products.app.domain.repository.SearchHistoryRepository
import com.products.app.util.MockDataFactory
import com.products.app.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetMatchingSearchesUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var searchHistoryRepository: SearchHistoryRepository

    private lateinit var useCase: GetMatchingSearchesUseCase

    @Before
    fun setUp() {
        useCase = GetMatchingSearchesUseCase(searchHistoryRepository)
    }

    @Test
    fun `invoke should call repository getMatchingSearches with correct parameters`() = runTest {

        val query = "iphone"
        val limit = 10
        val matchingSearches = MockDataFactory.createSearchHistoryList(3)
        whenever(searchHistoryRepository.getMatchingSearches(query, limit)).thenReturn(flowOf(matchingSearches))
        val result = useCase(query, limit).first()
        assert(result == matchingSearches)
        verify(searchHistoryRepository).getMatchingSearches(query, limit)
    }

    @Test
    fun `invoke with default limit should use default value`() = runTest {

        val query = "laptop"
        val defaultLimit = 10
        val matchingSearches = MockDataFactory.createSearchHistoryList(2)
        whenever(searchHistoryRepository.getMatchingSearches(query, defaultLimit)).thenReturn(flowOf(matchingSearches))
        val result = useCase(query).first()
        assert(result == matchingSearches)
        verify(searchHistoryRepository).getMatchingSearches(query, defaultLimit)
    }

    @Test
    fun `invoke with empty query should call repository`() = runTest {

        val emptyQuery = ""
        val limit = 5
        val matchingSearches = emptyList<SearchHistory>()
        whenever(searchHistoryRepository.getMatchingSearches(emptyQuery, limit)).thenReturn(flowOf(matchingSearches))
        val result = useCase(emptyQuery, limit).first()
        assert(result.isEmpty())
        verify(searchHistoryRepository).getMatchingSearches(emptyQuery, limit)
    }

    @Test
    fun `invoke with blank query should call repository`() = runTest {

        val blankQuery = "   "
        val limit = 5
        val matchingSearches = emptyList<SearchHistory>()
        whenever(searchHistoryRepository.getMatchingSearches(blankQuery, limit)).thenReturn(flowOf(matchingSearches))
        val result = useCase(blankQuery, limit).first()
        assert(result.isEmpty())
        verify(searchHistoryRepository).getMatchingSearches(blankQuery, limit)
    }

    @Test
    fun `invoke with special characters should call repository`() = runTest {

        val queryWithSpecialChars = "auriculares sony wh-1000xm5"
        val limit = 10
        val matchingSearches = MockDataFactory.createSearchHistoryList(1)
        whenever(searchHistoryRepository.getMatchingSearches(queryWithSpecialChars, limit)).thenReturn(flowOf(matchingSearches))
        val result = useCase(queryWithSpecialChars, limit).first()
        assert(result == matchingSearches)
        verify(searchHistoryRepository).getMatchingSearches(queryWithSpecialChars, limit)
    }

    @Test
    fun `invoke with very long query should call repository`() = runTest {

        val longQuery = "This is a very long search query that might exceed normal limits and should be handled correctly by the use case"
        val limit = 10
        val matchingSearches = emptyList<SearchHistory>()
        whenever(searchHistoryRepository.getMatchingSearches(longQuery, limit)).thenReturn(flowOf(matchingSearches))
        val result = useCase(longQuery, limit).first()
        assert(result.isEmpty())
        verify(searchHistoryRepository).getMatchingSearches(longQuery, limit)
    }

    @Test
    fun `invoke with zero limit should call repository`() = runTest {

        val query = "test"
        val zeroLimit = 0
        val matchingSearches = emptyList<SearchHistory>()
        whenever(searchHistoryRepository.getMatchingSearches(query, zeroLimit)).thenReturn(flowOf(matchingSearches))
        val result = useCase(query, zeroLimit).first()
        assert(result.isEmpty())
        verify(searchHistoryRepository).getMatchingSearches(query, zeroLimit)
    }

    @Test
    fun `invoke with negative limit should call repository`() = runTest {

        val query = "test"
        val negativeLimit = -5
        val matchingSearches = emptyList<SearchHistory>()
        whenever(searchHistoryRepository.getMatchingSearches(query, negativeLimit)).thenReturn(flowOf(matchingSearches))
        val result = useCase(query, negativeLimit).first()
        assert(result.isEmpty())
        verify(searchHistoryRepository).getMatchingSearches(query, negativeLimit)
    }

    @Test
    fun `invoke with large limit should call repository`() = runTest {

        val query = "test"
        val largeLimit = 1000
        val matchingSearches = MockDataFactory.createSearchHistoryList(5)
        whenever(searchHistoryRepository.getMatchingSearches(query, largeLimit)).thenReturn(flowOf(matchingSearches))
        val result = useCase(query, largeLimit).first()
        assert(result == matchingSearches)
        verify(searchHistoryRepository).getMatchingSearches(query, largeLimit)
    }

    @Test
    fun `multiple invocations with different parameters should call repository multiple times`() = runTest {

        val query1 = "query1"
        val query2 = "query2"
        val limit1 = 5
        val limit2 = 15
        val matchingSearches = MockDataFactory.createSearchHistoryList(2)
        whenever(searchHistoryRepository.getMatchingSearches(org.mockito.kotlin.any(), org.mockito.kotlin.any())).thenReturn(flowOf(matchingSearches))
        useCase(query1, limit1).first()
        useCase(query2, limit2).first()
        verify(searchHistoryRepository).getMatchingSearches(query1, limit1)
        verify(searchHistoryRepository).getMatchingSearches(query2, limit2)
    }
    @Test
    fun `invoke should return flow that emits matching searches`() = runTest {

        val query = "test query"
        val limit = 10
        val initialSearches = MockDataFactory.createSearchHistoryList(2)
        val updatedSearches = MockDataFactory.createSearchHistoryList(3)
        whenever(searchHistoryRepository.getMatchingSearches(query, limit))
            .thenReturn(flowOf(initialSearches, updatedSearches))
        val result = useCase(query, limit).first()
        assert(result == initialSearches)
        verify(searchHistoryRepository).getMatchingSearches(query, limit)
    }
}
