package com.products.app.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.products.app.domain.repository.SearchHistoryRepository
import com.products.app.util.MockDataFactory
import com.products.app.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class GetRecentSearchesUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: SearchHistoryRepository

    private lateinit var useCase: GetRecentSearchesUseCase

    @Before
    fun setUp() {
        useCase = GetRecentSearchesUseCase(repository)
    }

    @Test
    fun `when repository returns search history, should return flow with data`() = runTest {
        // Given
        val limit = 5
        val expectedSearchHistory = MockDataFactory.createSearchHistoryList(3)
        whenever(repository.getRecentSearches(limit)).thenReturn(flowOf(expectedSearchHistory))

        // When & Then
        useCase(limit).test {
            val actualHistory = awaitItem()
            assertThat(actualHistory).isEqualTo(expectedSearchHistory)
            assertThat(actualHistory).hasSize(3)
            awaitComplete()
        }

        verify(repository).getRecentSearches(limit)
    }

    @Test
    fun `when repository returns empty list, should return empty flow`() = runTest {
        // Given
        val limit = 10
        val emptyHistory = emptyList<com.products.app.domain.model.SearchHistory>()
        whenever(repository.getRecentSearches(limit)).thenReturn(flowOf(emptyHistory))

        // When & Then
        useCase(limit).test {
            val actualHistory = awaitItem()
            assertThat(actualHistory).isEmpty()
            awaitComplete()
        }

        verify(repository).getRecentSearches(limit)
    }

    @Test
    fun `when using default limit, should use default value`() = runTest {
        // Given
        val defaultLimit = 10
        val expectedHistory = MockDataFactory.createSearchHistoryList(5)
        whenever(repository.getRecentSearches(defaultLimit)).thenReturn(flowOf(expectedHistory))

        // When & Then
        useCase().test {
            val actualHistory = awaitItem()
            assertThat(actualHistory).hasSize(5)
            awaitComplete()
        }

        verify(repository).getRecentSearches(defaultLimit)
    }

    @Test
    fun `when limit is 1, should return single item`() = runTest {
        // Given
        val limit = 1
        val singleItemHistory = listOf(MockDataFactory.createSearchHistory("latest search"))
        whenever(repository.getRecentSearches(limit)).thenReturn(flowOf(singleItemHistory))

        // When & Then
        useCase(limit).test {
            val actualHistory = awaitItem()
            assertThat(actualHistory).hasSize(1)
            assertThat(actualHistory.first().query).isEqualTo("latest search")
            awaitComplete()
        }
    }

    @Test
    fun `when repository emits multiple values, should emit all values`() = runTest {
        // Given
        val limit = 5
        val firstEmission = MockDataFactory.createSearchHistoryList(2)
        val secondEmission = MockDataFactory.createSearchHistoryList(3)
        whenever(repository.getRecentSearches(limit)).thenReturn(
            flowOf(firstEmission, secondEmission)
        )

        // When & Then
        useCase(limit).test {
            val firstResult = awaitItem()
            assertThat(firstResult).hasSize(2)

            val secondResult = awaitItem()
            assertThat(secondResult).hasSize(3)

            awaitComplete()
        }
    }

    @Test
    fun `when limit is zero, should still call repository with zero`() = runTest {
        // Given
        val limit = 0
        val emptyHistory = emptyList<com.products.app.domain.model.SearchHistory>()
        whenever(repository.getRecentSearches(limit)).thenReturn(flowOf(emptyHistory))

        // When & Then
        useCase(limit).test {
            val actualHistory = awaitItem()
            assertThat(actualHistory).isEmpty()
            awaitComplete()
        }

        verify(repository).getRecentSearches(limit)
    }

    @Test
    fun `when search history is ordered by timestamp, should maintain order`() = runTest {
        // Given
        val limit = 3
        val currentTime = System.currentTimeMillis()
        val orderedHistory = listOf(
            MockDataFactory.createSearchHistory("newest", currentTime),
            MockDataFactory.createSearchHistory("middle", currentTime - 1000),
            MockDataFactory.createSearchHistory("oldest", currentTime - 2000)
        )
        whenever(repository.getRecentSearches(limit)).thenReturn(flowOf(orderedHistory))

        // When & Then
        useCase(limit).test {
            val actualHistory = awaitItem()
            assertThat(actualHistory[0].query).isEqualTo("newest")
            assertThat(actualHistory[1].query).isEqualTo("middle")
            assertThat(actualHistory[2].query).isEqualTo("oldest")
            
            // Verify timestamps are in descending order (newest first)
            assertThat(actualHistory[0].timestamp).isGreaterThan(actualHistory[1].timestamp)
            assertThat(actualHistory[1].timestamp).isGreaterThan(actualHistory[2].timestamp)
            
            awaitComplete()
        }
    }
}


