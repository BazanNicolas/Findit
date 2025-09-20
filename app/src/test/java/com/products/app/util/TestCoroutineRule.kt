package com.products.app.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * JUnit rule for coroutine testing.
 * 
 * This rule replaces the Main dispatcher with a TestDispatcher to ensure
 * deterministic and fast execution of coroutine-based tests. It automatically
 * sets up the test dispatcher before each test and cleans up afterward.
 * 
 * The UnconfinedTestDispatcher is used by default, which executes coroutines
 * immediately on the current thread, making tests run synchronously and
 * predictably.
 * 
 * Usage:
 * ```kotlin
 * @get:Rule
 * val testCoroutineRule = TestCoroutineRule()
 * ```
 */
@ExperimentalCoroutinesApi
class TestCoroutineRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    
    /**
     * Sets up the test dispatcher before each test runs.
     * 
     * @param description The test description
     */
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }
    
    /**
     * Cleans up the test dispatcher after each test completes.
     * 
     * @param description The test description
     */
    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
