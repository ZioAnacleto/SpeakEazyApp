package com.zioanacleto.speakeazy.logger

import com.zioanacleto.buffa.default
import com.zioanacleto.buffa.logging.AnacletoLevel
import com.zioanacleto.buffa.logging.CustomAnacletoLogger
import timber.log.Timber

class LocalLogger : CustomAnacletoLogger {
    override val tag: String = this::class.simpleName.default()

    override fun init() {
        if (Timber.treeCount < 1)
            Timber.plant(Timber.DebugTree())
    }

    override fun log(message: String, error: Throwable?, level: AnacletoLevel) {
        when (level) {
            AnacletoLevel.ERROR -> Timber.Forest.e(error, "❌ $message")
            AnacletoLevel.WARNING -> Timber.Forest.w(error, "⚠️ $message")
            AnacletoLevel.INFO -> Timber.Forest.i(error, "ℹ️ $message")
            AnacletoLevel.DEBUG -> Timber.Forest.d(error, "📝 $message")
            AnacletoLevel.VERBOSE -> Timber.Forest.v(error, "📣 $message")
        }
    }
}