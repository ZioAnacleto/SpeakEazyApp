package com.zioanacleto.speakeazy.core.analytics.traces

import com.google.firebase.Firebase
import com.google.firebase.perf.metrics.Trace
import com.google.firebase.perf.performance
import kotlin.reflect.KClass

/**
 *  This [PerformanceTracesManager] implementation provides two methods, one for starting tracing
 *  an event, and one for stop the trace
 *
 *  **startTrace** - if provided trace is not registered, it will be added to a local map.
 *
 *  **stopTrace** - if provided trace name is not found in local map, the trace would not be stopped.
 */
class FirebasePerformanceTracesManager : PerformanceTracesManager {
    private val registeredTraces: MutableMap<String, Trace> = mutableMapOf()

    override fun startTrace(sourceClazz: KClass<*>, traceName: String) {
        val completeTraceName = "${sourceClazz.simpleName}_$traceName"
        val trace = Firebase.performance.newTrace(completeTraceName)
        if (!registeredTraces.keys.contains(completeTraceName))
            registeredTraces[completeTraceName] = trace
        trace.start()
    }

    override fun stopTrace(sourceClazz: KClass<*>, traceName: String) {
        val completeTraceName = "${sourceClazz.simpleName}_$traceName"
        if (registeredTraces.keys.contains(completeTraceName))
            registeredTraces[completeTraceName]?.stop()
    }
}