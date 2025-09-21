package com.products.app.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
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
        assertThat(recentSearches).isInstanceOf(com.products.app.core.AppResult.Success::class.java)
        
        val successResult = recentSearches as com.products.app.core.AppResult.Success
        assertThat(successResult.data).isNotEmpty()
        assertThat(successResult.data.first().query).isEqualTo(query)
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
        assertThat(recentSearches).isInstanceOf(com.products.app.core.AppResult.Success::class.java)
        
        val successResult = recentSearches as com.products.app.core.AppResult.Success
        val savedQueries = successResult.data.map { it.query }
        
        // Should be in reverse chronological order (most recent first)
        assertThat(savedQueries).isEqualTo(queries.reversed())
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
        assertThat(recentSearches).isInstanceOf(com.products.app.core.AppResult.Success::class.java)
        
        val successResult = recentSearches as com.products.app.core.AppResult.Success
        assertThat(successResult.data.size).isEqualTo(limit)
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
        repository.deleteSearch(queryToDelete)

        // Then
        val recentSearches = repository.getRecentSearches(10)
        assertThat(recentSearches).isInstanceOf(com.products.app.core.AppResult.Success::class.java)
        
        val successResult = recentSearches as com.products.app.core.AppResult.Success
        val savedQueries = successResult.data.map { it.query }
        
        assertThat(savedQueries).contains(queryToKeep)
        assertThat(savedQueries).doesNotContain(queryToDelete)
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
        assertThat(recentSearches).isInstanceOf(com.products.app.core.AppResult.Success::class.java)
        
        val successResult = recentSearches as com.products.app.core.AppResult.Success
        assertThat(successResult.data).isEmpty()
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
        assertThat(recentSearches).isInstanceOf(com.products.app.core.AppResult.Success::class.java)
        
        val successResult = recentSearches as com.products.app.core.AppResult.Success
        val savedQueries = successResult.data.map { it.query }
        
        // Should have two entries for the same query
        assertThat(savedQueries.filter { it == query }).hasSize(2)
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

        // Then
        assertThat(matchingSearches).isInstanceOf(com.products.app.core.AppResult.Success::class.java)
        
        val successResult = matchingSearches as com.products.app.core.AppResult.Success
        val matchingQueries = successResult.data.map { it.query }
        
        assertThat(matchingQueries).contains("iPhone 15")
        assertThat(matchingQueries).contains("iPhone 14")
        assertThat(matchingQueries).doesNotContain("Samsung Galaxy")
        assertThat(matchingQueries).doesNotContain("iPad Pro")
    }

    /**
     * Test that the repository handles empty database gracefully.
     */
    @Test
    fun getRecentSearches_withEmptyDatabase_shouldReturnEmptyList() = runTest {
        // When
        val recentSearches = repository.getRecentSearches(10)

        // Then
        assertThat(recentSearches).isInstanceOf(com.products.app.core.AppResult.Success::class.java)
        
        val successResult = recentSearches as com.products.app.core.AppResult.Success
        assertThat(successResult.data).isEmpty()
    }
}
