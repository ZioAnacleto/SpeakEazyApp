package com.zioanacleto.speakeazy.core.data.search

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zioanacleto.speakeazy.core.database.dao.SearchDao
import java.util.concurrent.TimeUnit

class SearchQueriesCleanUpWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val searchDao: SearchDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val threshold = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(THRESHOLD_TEN_DAYS)
        searchDao.deleteQueriesOlderThan(threshold)

        return Result.success()
    }

    companion object {
        private const val THRESHOLD_TEN_DAYS = 10L
    }
}