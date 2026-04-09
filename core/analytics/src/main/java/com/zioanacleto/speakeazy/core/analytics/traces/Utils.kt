package com.zioanacleto.speakeazy.core.analytics.traces

import kotlin.reflect.KClass

fun PerformanceTracesManager.trace(sourceClazz: KClass<*>, traceName: String, block: () -> Unit) {
    startTrace(sourceClazz, traceName)
    block()
    stopTrace(sourceClazz, traceName)
}

suspend fun PerformanceTracesManager.traceSuspend(
    sourceClazz: KClass<*>,
    traceName: String,
    block: suspend () -> Unit
) {
    startTrace(sourceClazz, traceName)
    block()
    stopTrace(sourceClazz, traceName)
}

suspend fun <T> PerformanceTracesManager.returningTraceSuspend(
    sourceClazz: KClass<*>,
    traceName: String,
    block: suspend () -> T
): T {
    startTrace(sourceClazz, traceName)
    val response = block()
    stopTrace(sourceClazz, traceName)

    return response
}

fun <T> PerformanceTracesManager.returningTrace(
    sourceClazz: KClass<*>,
    traceName: String,
    block: () -> T
): T {
    startTrace(sourceClazz, traceName)
    val response = block()
    stopTrace(sourceClazz, traceName)

    return response
}