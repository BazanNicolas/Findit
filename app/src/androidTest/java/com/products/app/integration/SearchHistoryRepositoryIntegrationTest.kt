package com.products.app.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import com.products.app.data.local.database.ProductsDatabase
import com.products.app.data.repository.SearchHistoryRepositoryImpl
import com.products.app.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Integration test for SearchHistoryRepository that tests the actual integration
 * between the repository and the Room database.
 * 
 * This test verifies that the repository correctly handles database operations,
 * including saving, retrieving, and managing search history data.
 * 
 * Uses an in-memory database for testing to ensure test isolation and performance.
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class SearchHistoryRepositoryIntegrationTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: SearchHistoryRepository
    private lateinit var database: ProductsDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        
        // Create in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
            context,
            ProductsDatabase::class.java
        ).allowMainThreadQueries()
         .build()

        repository = SearchHistoryRepositoryImpl(database.searchHistoryDao())
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    /**
     * Test that the repository can save a search query successfully.
     */
    @Test
    fun saveSearch_withValidQuery_shouldSaveSuccessfully() = runTest {
        // Given
        val query = "iPhone 15"

        // When
        repository.saveSearch(query)

        // Then
        val recentSearches = repository.getRecentSearches(10)
        var searchList: List<com.products.app.domain.model.SearchHistory> = emptyList()
        recentSearches.collect { searchList = it }
        
        assertFalse(searchList.isEmpty())
        assertEquals(query, searchList.first().query)
    }

    /**
     * Test that the repository returns searches in chronological order (most recent first).
     */
    @Test
    fun getRecentSearches_shouldReturnInChronologicalOrder() = runTest {
        // Given
        val queries = listOf("iPhone", "Samsung", "Laptop", "Tablet")
        
        // When
        queries.forEach { query ->
            repository.saveSearch(query)
            // Small delay to ensure different timestamps
            Thread.sleep(10)
        }

        // Then
        val recentSearches = repository.getRecentSearches(10)
        var searchList: List<com.products.app.domain.model.SearchHistory> = emptyList()
        recentSearches.collect { searchList = it }
        
        val savedQueries = searchList.map { it.query }
        
        // Should be in reverse chronological order (most recent first)
        assertEquals(queries.reversed(), savedQueries)
    }

    /**
     * Test that the repository limits the number of returned searches correctly.
     */
    @Test
    fun getRecentSearches_withLimit_shouldReturnCorrectNumberOfResults() = runTest {
        // Given
        val queries = listOf("Query1", "Query2", "Query3", "Query4", "Query5")
        val limit = 3
        
        // When
        queries.forEach { query ->
            repository.saveSearch(query)
            Thread.sleep(10)
        }

        // Then
        val recentSearches = repository.getRecentSearches(limit)
        var searchList: List<com.products.app.domain.model.SearchHistory> = emptyList()
        recentSearches.collect { searchList = it }
        
        assertEquals(limit, searchList.size)
    }

    /**
     * Test that the repository can delete a specific search.
     */
    @Test
    fun deleteSearch_withValidQuery_shouldDeleteSuccessfully() = runTest {
        // Given
        val queryToDelete = "iPhone"
        val queryToKeep = "Samsung"
        
        repository.saveSearch(queryToDelete)
        repository.saveSearch(queryToKeep)

        // When
        val recentSearchesBefore = repository.getRecentSearches(10)
        var searchListBefore: List<com.products.app.domain.model.SearchHistory> = emptyList()
        recentSearchesBefore.collect { searchListBefore = it }
        
        val searchToDelete = searchListBefore.find { it.query == queryToDelete }
        if (searchToDelete != null) {
            repository.deleteSearch(searchToDelete)
        }

        // Then
        val recentSearches = repository.getRecentSearches(10)
        var searchList: List<com.products.app.domain.model.SearchHistory> = emptyList()
        recentSearches.collect { searchList = it }
        
        val savedQueries = searchList.map { it.query }
        assertTrue(savedQueries.contains(queryToKeep))
        assertFalse(savedQueries.contains(queryToDelete))
    }

    /**
     * Test that the repository can clear all searches.
     */
    @Test
    fun clearAllSearches_shouldRemoveAllSearches() = runTest {
        // Given
        val queries = listOf("Query1", "Query2", "Query3")
        queries.forEach { query ->
            repository.saveSearch(query)
        }

        // When
        repository.clearAllSearches()

        // Then
        val recentSearches = repository.getRecentSearches(10)
        var searchList: List<com.products.app.domain.model.SearchHistory> = emptyList()
        recentSearches.collect { searchList = it }
        
        assertTrue(searchList.isEmpty())
    }

    /**
     * Test that the repository handles duplicate searches correctly.
     */
    @Test
    fun saveSearch_withDuplicateQuery_shouldCreateNewEntry() = runTest {
        // Given
        val query = "iPhone"
        
        // When
        repository.saveSearch(query)
        Thread.sleep(10)
        repository.saveSearch(query) // Duplicate

        // Then
        val recentSearches = repository.getRecentSearches(10)
        var searchList: List<com.products.app.domain.model.SearchHistory> = emptyList()
        recentSearches.collect { searchList = it }
        
        val savedQueries = searchList.map { it.query }
        
        // Should have two entries for the same query
        assertEquals(2, savedQueries.filter { it == query }.size)
    }

    /**
     * Test that the repository can get matching searches for autocomplete.
     */
    @Test
    fun getMatchingSearches_withPartialQuery_shouldReturnMatchingResults() = runTest {
        // Given
        val searches = listOf("iPhone 15", "iPhone 14", "Samsung Galaxy", "iPad Pro")
        searches.forEach { search ->
            repository.saveSearch(search)
            Thread.sleep(10)
        }

        // When
        val matchingSearches = repository.getMatchingSearches("iPhone")
        var matchingList: List<com.products.app.domain.model.SearchHistory> = emptyList()
        matchingSearches.collect { matchingList = it }

        // Then
        val matchingQueries = matchingList.map { it.query }
        
        assertTrue(matchingQueries.contains("iPhone 15"))
        assertTrue(matchingQueries.contains("iPhone 14"))
        assertFalse(matchingQueries.contains("Samsung Galaxy"))
        assertFalse(matchingQueries.contains("iPad Pro"))
    }

    /**
     * Test that the repository handles empty database gracefully.
     */
    @Test
    fun getRecentSearches_withEmptyDatabase_shouldReturnEmptyList() = runTest {
        // When
        val recentSearches = repository.getRecentSearches(10)
        var searchList: List<com.products.app.domain.model.SearchHistory> = emptyList()
        recentSearches.collect { searchList = it }

        // Then
        assertTrue(searchList.isEmpty())
    }
}