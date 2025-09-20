package com.products.app.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.products.app.core.AppResult
import com.products.app.domain.repository.ProductDetailRepository
import com.products.app.util.MockDataFactory
import com.products.app.util.TestCoroutineRule
import com.products.app.util.assertSuccess
import com.products.app.util.assertError
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

        val productId = "MLA123456789"
        val expectedProductDetail = MockDataFactory.createProductDetail(id = productId)
        whenever(repository.getProductDetail(productId)).thenReturn(AppResult.Success(expectedProductDetail))
        val result = useCase(productId)
        val actualProductDetail = result.assertSuccess()
        assertThat(actualProductDetail).isEqualTo(expectedProductDetail)
        assertThat(actualProductDetail.id).isEqualTo(productId)
        verify(repository).getProductDetail(productId)
    }

    @Test
    fun `when product does not exist, should return failure result`() = runTest {

        val productId = "MLA999999999"
        val exception = Exception("Product not found")
        whenever(repository.getProductDetail(productId)).thenReturn(AppResult.Error("Product not found"))
        val result = useCase(productId)
        val errorMessage = result.assertError("Product not found")
        verify(repository).getProductDetail(productId)
    }

    @Test
    fun `when repository throws network exception, should return failure result`() = runTest {

        val productId = "MLA123456789"
        val networkException = Exception("Network timeout")
        whenever(repository.getProductDetail(productId)).thenReturn(AppResult.Error("Network timeout"))
        val result = useCase(productId)
        val errorMessage = result.assertError()
        assertThat(errorMessage).contains("Network timeout")
    }

    @Test
    fun `when product id is empty, should still call repository`() = runTest {

        val emptyProductId = ""
        val exception = Exception("Invalid product ID")
        whenever(repository.getProductDetail(emptyProductId)).thenReturn(AppResult.Error("Invalid product ID"))
        val result = useCase(emptyProductId)
        val errorMessage = result.assertError("Invalid product ID")
        verify(repository).getProductDetail(emptyProductId)
    }

    @Test
    fun `when product detail has complete information, should return all data`() = runTest {

        val productId = "MLA987654321"
        val completeProductDetail = MockDataFactory.createProductDetail(
            id = productId,
            name = "Samsung Galaxy S24 Ultra 512GB"
        )
        whenever(repository.getProductDetail(productId)).thenReturn(AppResult.Success(completeProductDetail))
        val result = useCase(productId)
        val productDetail = result.assertSuccess()!!
        assertThat(productDetail.id).isEqualTo(productId)
        assertThat(productDetail.name).isEqualTo("Samsung Galaxy S24 Ultra 512GB")
        assertThat(productDetail.pictures).isNotEmpty()
        assertThat(productDetail.attributes).isNotEmpty()
        assertThat(productDetail.shortDescription?.content).isNotNull()
    }

    @Test
    fun `when multiple calls are made with same product id, should call repository each time`() = runTest {

        val productId = "MLA555555555"
        val productDetail = MockDataFactory.createProductDetail(id = productId)
        whenever(repository.getProductDetail(productId)).thenReturn(AppResult.Success(productDetail))
        val result1 = useCase(productId)
        val result2 = useCase(productId)
        result1.assertSuccess()
        result2.assertSuccess()
        verify(repository, org.mockito.kotlin.times(2)).getProductDetail(productId)
    }
}
