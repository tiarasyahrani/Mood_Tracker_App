package com.nilam.moodtrackerapp.utils

import android.content.Context
import androidx.work.*
import com.nilam.moodtrackerapp.worker.ReminderWorker
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first
import com.nilam.moodtrackerapp.data.ReminderPreferences

object ReminderScheduler {

    fun scheduleDailyReminder(context: Context) {

        val time = runBlocking {
            ReminderPreferences.getReminderTime(context).first()
        }

        val (hour, minute) = time.split(":").map { it.toInt() }

        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(now)) add(Calendar.DATE, 1)
        }

        val delay = target.timeInMillis - now.timeInMillis

        // HAPUS jadwal sebelumnya
        WorkManager.getInstance(context).cancelUniqueWork("daily_reminder")

        val work = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork("daily_reminder", ExistingWorkPolicy.REPLACE, work)
    }
}
