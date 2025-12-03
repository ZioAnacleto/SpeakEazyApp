package com.zioanacleto.speakeazy

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

class TestDispatcherProvider(
    private val dispatcher: TestDispatcher = StandardTestDispatcher()
): DispatcherProvider {
    override fun default(): CoroutineDispatcher = dispatcher
    override fun io(): CoroutineDispatcher = dispatcher
    override fun main(): CoroutineDispatcher = dispatcher
    override fun unconfined(): CoroutineDispatcher = dispatcher
}