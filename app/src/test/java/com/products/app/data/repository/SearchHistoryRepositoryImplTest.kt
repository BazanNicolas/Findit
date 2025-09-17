package com.products.app.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.products.app.data.local.dao.SearchHistoryDao
import com.products.app.data.local.entity.SearchHistoryEntity
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
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SearchHistoryRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var dao: SearchHistoryDao

    private lateinit var repository: SearchHistoryRepositoryImpl

    @Before
    fun setUp() {
        repository = SearchHistoryRepositoryImpl(dao)
    }

    @Test
    fun `when getting recent searches, should return mapped domain models`() = runTest {
        // Given
        val limit = 5
        val entities = listOf(
            SearchHistoryEntity("iphone", System.currentTimeMillis()),
            SearchHistoryEntity("samsung", System.currentTimeMillis() - 1000),
            SearchHistoryEntity("laptop", System.currentTimeMillis() - 2000)
        )
        whenever(dao.getRecentSearches(limit)).thenReturn(flowOf(entities))

        // When & Then
        repository.getRecentSearches(limit).test {
            val result = awaitItem()
            assertThat(result).hasSize(3)
            assertThat(result[0].query).isEqualTo("iphone")
            assertThat(result[1].query).isEqualTo("samsung")
            assertThat(result[2].query).isEqualTo("laptop")
            awaitComplete()
        }

        verify(dao).getRecentSearches(limit)
    }

    @Test
    fun `when dao returns empty flow, should return empty domain list`() = runTest {
        // Given
        val limit = 10
        whenever(dao.getRecentSearches(limit)).thenReturn(flowOf(emptyList()))

        // When & Then
        repository.getRecentSearches(limit).test {
            val result = awaitItem()
            assertThat(result).isEmpty()
            awaitComplete()
        }
    }

    @Test
    fun `when saving search, should save entity with current timestamp`() = runTest {
        // Given
        val query = "new search"
        val beforeSave = System.currentTimeMillis()

        // When
        repository.saveSearch(query)

        // Then
        val entityCaptor = argumentCaptor<SearchHistoryEntity>()
        verify(dao).insertSearch(entityCaptor.capture())
        
        val savedEntity = entityCaptor.firstValue
        assertThat(savedEntity.query).isEqualTo(query)
        assertThat(savedEntity.timestamp).isAtLeast(beforeSave)
        assertThat(savedEntity.timestamp).isAtMost(System.currentTimeMillis())
    }

    @Test
    fun `when saving search with spaces, should trim query`() = runTest {
        // Given
        val queryWithSpaces = "  search with spaces  "
        val expectedTrimmedQuery = "search with spaces"

        // When
        repository.saveSearch(queryWithSpaces)

        // Then
        val entityCaptor = argumentCaptor<SearchHistoryEntity>()
        verify(dao).insertSearch(entityCaptor.capture())
        
        val savedEntity = entityCaptor.firstValue
        assertThat(savedEntity.query).isEqualTo(expectedTrimmedQuery)
    }

    @Test
    fun `when getting matching searches, should return filtered results`() = runTest {
        // Given
        val query = "iphone"
        val limit = 5
        val matchingEntities = listOf(
            SearchHistoryEntity("iphone 15", System.currentTimeMillis()),
            SearchHistoryEntity("iphone pro", System.currentTimeMillis() - 1000)
        )
        whenever(dao.getMatchingSearches(query, limit)).thenReturn(flowOf(matchingEntities))

        // When & Then
        repository.getMatchingSearches(query, limit).test {
            val result = awaitItem()
            assertThat(result).hasSize(2)
            assertThat(result[0].query).isEqualTo("iphone 15")
            assertThat(result[1].query).isEqualTo("iphone pro")
            awaitComplete()
        }

        verify(dao).getMatchingSearches(query, limit)
    }

    @Test
    fun `when clearing search history, should call dao clear method`() = runTest {
        // When
        repository.clearAllSearches()

        // Then
        verify(dao).clearAllSearches()
    }

    @Test
    fun `when dao emits multiple values, should emit all mapped values`() = runTest {
        // Given
        val limit = 3
        val firstEmission = listOf(
            SearchHistoryEntity("first", System.currentTimeMillis())
        )
        val secondEmission = listOf(
            SearchHistoryEntity("first", System.currentTimeMillis()),
            SearchHistoryEntity("second", System.currentTimeMillis() - 1000)
        )
        whenever(dao.getRecentSearches(limit)).thenReturn(
            flowOf(firstEmission, secondEmission)
        )

        // When & Then
        repository.getRecentSearches(limit).test {
            val firstResult = awaitItem()
            assertThat(firstResult).hasSize(1)
            assertThat(firstResult[0].query).isEqualTo("first")

            val secondResult = awaitItem()
            assertThat(secondResult).hasSize(2)
            assertThat(secondResult[0].query).isEqualTo("first")
            assertThat(secondResult[1].query).isEqualTo("second")

            awaitComplete()
        }
    }

    @Test
    fun `when saving multiple searches, should save each with unique timestamp`() = runTest {
        // Given
        val query1 = "first search"
        val query2 = "second search"

        // When
        repository.saveSearch(query1)
        Thread.sleep(1) // Ensure different timestamps
        repository.saveSearch(query2)

        // Then
        val entityCaptor = argumentCaptor<SearchHistoryEntity>()
        verify(dao, org.mockito.kotlin.times(2)).insertSearch(entityCaptor.capture())
        
        val savedEntities = entityCaptor.allValues
        assertThat(savedEntities[0].query).isEqualTo(query1)
        assertThat(savedEntities[1].query).isEqualTo(query2)
        assertThat(savedEntities[1].timestamp).isAtLeast(savedEntities[0].timestamp)
    }
}
