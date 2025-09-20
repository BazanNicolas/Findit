package com.products.app.domain.usecase

import com.products.app.domain.repository.SearchHistoryRepository
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
class ClearAllSearchesUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var searchHistoryRepository: SearchHistoryRepository

    private lateinit var useCase: ClearAllSearchesUseCase

    @Before
    fun setUp() {
        useCase = ClearAllSearchesUseCase(searchHistoryRepository)
    }

    @Test
    fun `invoke should call repository clearAllSearches`() = runTest {

        useCase()
        verify(searchHistoryRepository).clearAllSearches()
    }
    @Test
    fun `multiple invocations should call repository multiple times`() = runTest {

        useCase()
        useCase()
        verify(searchHistoryRepository, org.mockito.kotlin.times(2)).clearAllSearches()
    }
}
