package com.products.app.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.products.app.domain.repository.ProductDetailRepository
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
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetProductDetailUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: ProductDetailRepository

    private lateinit var useCase: GetProductDetailUseCase

    @Before
    fun setUp() {
        useCase = GetProductDetailUseCase(repository)
    }

    @Test
    fun `when product exists, should return product detail successfully`() = runTest {
        // Given
        val productId = "MLA123456789"
        val expectedProductDetail = MockDataFactory.createProductDetail(id = productId)
        whenever(repository.getProductDetail(productId)).thenReturn(Result.success(expectedProductDetail))

        // When
        val result = useCase(productId)

        // Then
        assertThat(result.isSuccess).isTrue()
        val actualProductDetail = result.getOrNull()
        assertThat(actualProductDetail).isEqualTo(expectedProductDetail)
        assertThat(actualProductDetail?.id).isEqualTo(productId)
        verify(repository).getProductDetail(productId)
    }

    @Test
    fun `when product does not exist, should return failure result`() = runTest {
        // Given
        val productId = "MLA999999999"
        val exception = Exception("Product not found")
        whenever(repository.getProductDetail(productId)).thenReturn(Result.failure(exception))

        // When
        val result = useCase(productId)

        // Then
        assertThat(result.isFailure).isTrue()
        val actualException = result.exceptionOrNull()
        assertThat(actualException?.message).isEqualTo("Product not found")
        verify(repository).getProductDetail(productId)
    }

    @Test
    fun `when repository throws network exception, should return failure result`() = runTest {
        // Given
        val productId = "MLA123456789"
        val networkException = Exception("Network timeout")
        whenever(repository.getProductDetail(productId)).thenReturn(Result.failure(networkException))

        // When
        val result = useCase(productId)

        // Then
        assertThat(result.isFailure).isTrue()
        val actualException = result.exceptionOrNull()
        assertThat(actualException?.message).contains("Network timeout")
    }

    @Test
    fun `when product id is empty, should still call repository`() = runTest {
        // Given
        val emptyProductId = ""
        val exception = Exception("Invalid product ID")
        whenever(repository.getProductDetail(emptyProductId)).thenReturn(Result.failure(exception))

        // When
        val result = useCase(emptyProductId)

        // Then
        assertThat(result.isFailure).isTrue()
        verify(repository).getProductDetail(emptyProductId)
    }

    @Test
    fun `when product detail has complete information, should return all data`() = runTest {
        // Given
        val productId = "MLA987654321"
        val completeProductDetail = MockDataFactory.createProductDetail(
            id = productId,
            name = "Samsung Galaxy S24 Ultra 512GB"
        )
        whenever(repository.getProductDetail(productId)).thenReturn(Result.success(completeProductDetail))

        // When
        val result = useCase(productId)

        // Then
        assertThat(result.isSuccess).isTrue()
        val productDetail = result.getOrNull()!!
        assertThat(productDetail.id).isEqualTo(productId)
        assertThat(productDetail.name).isEqualTo("Samsung Galaxy S24 Ultra 512GB")
        assertThat(productDetail.pictures).isNotEmpty()
        assertThat(productDetail.attributes).isNotEmpty()
        assertThat(productDetail.shortDescription?.content).isNotNull()
    }

    @Test
    fun `when multiple calls are made with same product id, should call repository each time`() = runTest {
        // Given
        val productId = "MLA555555555"
        val productDetail = MockDataFactory.createProductDetail(id = productId)
        whenever(repository.getProductDetail(productId)).thenReturn(Result.success(productDetail))

        // When
        val result1 = useCase(productId)
        val result2 = useCase(productId)

        // Then
        assertThat(result1.isSuccess).isTrue()
        assertThat(result2.isSuccess).isTrue()
        verify(repository, org.mockito.kotlin.times(2)).getProductDetail(productId)
    }
}


