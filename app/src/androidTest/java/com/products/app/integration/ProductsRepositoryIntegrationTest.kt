package com.products.app.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import com.products.app.core.NetworkErrorHandler
import com.products.app.data.repository.ProductsRepositoryImpl
import com.products.app.data.remote.ProductsApi
import com.products.app.domain.repository.ProductsRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Integration test for ProductsRepository that tests the actual integration
 * between the repository and the real MercadoLibre API.
 * 
 * This test verifies that the repository correctly handles real API responses,
 * error scenarios, and data transformation from DTOs to domain models.
 * 
 * Note: This test requires an active internet connection and will make
 * real HTTP calls to the MercadoLibre API.
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class ProductsRepositoryIntegrationTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: ProductsRepository
    private lateinit var productsApi: ProductsApi
    private lateinit var errorHandler: NetworkErrorHandler

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        
        // Create real network components
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.mercadolibre.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        productsApi = retrofit.create(ProductsApi::class.java)
        errorHandler = NetworkErrorHandler(context)
        repository = ProductsRepositoryImpl(productsApi, errorHandler)
    }

    @After
    fun tearDown() {
        // Clean up if needed
    }

    /**
     * Test that the repository can successfully search for products
     * using the real MercadoLibre API.
     */
    @Test
    fun searchProducts_withValidQuery_shouldReturnSuccess() = runTest {
        // Given
        val query = "iPhone"
        val offset = 0
        val limit = 20

        // When
        val result = repository.search(query, offset, limit)

        // Then
        assertTrue(result is com.products.app.core.AppResult.Success)
        
        val successResult = result as com.products.app.core.AppResult.Success<com.products.app.domain.model.ProductSearchResult>
        assertFalse(successResult.data.products.isEmpty())
        assertTrue(successResult.data.products.first().name.contains("iPhone"))
        assertNotNull(successResult.data.paging)
    }

    /**
     * Test that the repository handles empty search results correctly.
     */
    @Test
    fun searchProducts_withNonExistentQuery_shouldReturnEmptyResults() = runTest {
        // Given
        val query = "xyzqwertyuiopasdfghjkl123456789"
        val offset = 0
        val limit = 20

        // When
        val result = repository.search(query, offset, limit)

        // Then
        assertTrue(result is com.products.app.core.AppResult.Success)
        
        val successResult = result as com.products.app.core.AppResult.Success<com.products.app.domain.model.ProductSearchResult>
        assertTrue(successResult.data.products.isEmpty())
    }

    /**
     * Test that the repository handles pagination correctly.
     */
    @Test
    fun searchProducts_withPagination_shouldReturnCorrectResults() = runTest {
        // Given
        val query = "laptop"
        val firstPageOffset = 0
        val secondPageOffset = 20
        val limit = 10

        // When
        val firstPageResult = repository.search(query, firstPageOffset, limit)
        val secondPageResult = repository.search(query, secondPageOffset, limit)

        // Then
        assertTrue(firstPageResult is com.products.app.core.AppResult.Success)
        assertTrue(secondPageResult is com.products.app.core.AppResult.Success)
        
        val firstPage = firstPageResult as com.products.app.core.AppResult.Success<com.products.app.domain.model.ProductSearchResult>
        val secondPage = secondPageResult as com.products.app.core.AppResult.Success<com.products.app.domain.model.ProductSearchResult>
        
        // Results should be different (different products)
        val firstPageIds = firstPage.data.products.map { it.id }
        val secondPageIds = secondPage.data.products.map { it.id }
        
        assertNotEquals(firstPageIds, secondPageIds)
    }

    /**
     * Test that the repository handles invalid parameters gracefully.
     */
    @Test
    fun searchProducts_withInvalidParameters_shouldHandleGracefully() = runTest {
        // Given
        val query = ""
        val offset = -1
        val limit = 0

        // When
        val result = repository.search(query, offset, limit)

        // Then
        // The API might return empty results or the repository might handle it
        // This test verifies that no exceptions are thrown
        assertNotNull(result)
    }

    /**
     * Test that the repository can handle large result sets.
     */
    @Test
    fun searchProducts_withLargeLimit_shouldReturnResults() = runTest {
        // Given
        val query = "smartphone"
        val offset = 0
        val limit = 50

        // When
        val result = repository.search(query, offset, limit)

        // Then
        assertTrue(result is com.products.app.core.AppResult.Success)
        
        val successResult = result as com.products.app.core.AppResult.Success<com.products.app.domain.model.ProductSearchResult>
        assertFalse(successResult.data.products.isEmpty())
        assertTrue(successResult.data.products.size <= limit)
    }
}
