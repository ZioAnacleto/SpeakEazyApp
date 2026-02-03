package com.zioanacleto.speakeazy

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertTrue

/**
 *  This implies that all conditions must be true for the test to pass
 */
fun assertAllTrue(
    vararg condition: Boolean,
) = condition.forEachIndexed { index, it ->
    assertTrue("Condition ${index + 1} failed", it)
}

/**
 *  Utility function that retrieves first value and second value after [dropCount] emissions
 *  and return them inside a [Pair]
 */
suspend fun <T> Flow<T>.testResourceFlow(
    dropCount: Int = 1
): Pair<T, T> = this.zip(this.drop(dropCount)) { first, second ->
    first to second
}.first()

fun TestScope.testDispatcher() = TestDispatcherProvider(StandardTestDispatcher(testScheduler))