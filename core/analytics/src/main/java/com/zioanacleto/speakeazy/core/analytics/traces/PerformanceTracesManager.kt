package com.zioanacleto.speakeazy.core.analytics.traces

import kotlin.reflect.KClass

interface PerformanceTracesManager {
    fun startTrace(sourceClazz: KClass<*>, traceName: String)
    fun stopTrace(sourceClazz: KClass<*>, traceName: String)
}