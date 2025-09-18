package com.products.app.util

import com.google.common.truth.Truth.assertThat
import com.products.app.core.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

/**
 * Extensiones útiles para testing
 */

/**
 * Verifica que un AppResult sea Success y contiene el valor esperado
 */
fun <T> AppResult<T>.assertSuccess(): T {
    assertThat(this).isInstanceOf(AppResult.Success::class.java)
    return (this as AppResult.Success).data
}

/**
 * Verifica que un AppResult sea Error y contiene el mensaje esperado
 */
fun <T> AppResult<T>.assertError(expectedMessage: String? = null): String {
    assertThat(this).isInstanceOf(AppResult.Error::class.java)
    val actualMessage = (this as AppResult.Error).message
    if (expectedMessage != null) {
        assertThat(actualMessage).isEqualTo(expectedMessage)
    }
    return actualMessage
}

/**
 * Extensión para obtener el primer valor de un Flow en tests
 */
suspend fun <T> Flow<T>.firstValue(): T = this.first()

/**
 * Extensión para ejecutar tests con corrutinas de forma más limpia
 */
fun runTestWithDispatcher(testBody: suspend () -> Unit) = runTest {
    testBody()
}

/**
 * Verifica que una lista no esté vacía y contiene el número esperado de elementos
 */
fun <T> List<T>.assertNotEmptyAndSize(expectedSize: Int) {
    assertThat(this).isNotEmpty()
    assertThat(this).hasSize(expectedSize)
}

/**
 * Verifica que una lista esté vacía
 */
fun <T> List<T>.assertEmpty() {
    assertThat(this).isEmpty()
}

/**
 * Verifica que un string no esté vacío
 */
fun String.assertNotBlank() {
    assertThat(this).isNotEmpty()
}

/**
 * Verifica que un valor no sea null
 */
fun <T> T?.assertNotNull(): T {
    assertThat(this).isNotNull()
    return this!!
}


