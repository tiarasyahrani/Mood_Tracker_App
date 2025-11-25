package com.nilam.moodtrackerapp.worker

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nilam.moodtrackerapp.utils.NotificationUtils
import com.nilam.moodtrackerapp.utils.ReminderScheduler

class ReminderWorker(
    private val ctx: Context,
    params: WorkerParameters
) : Worker(ctx, params) {

    override fun doWork(): Result {

        // Cek permission
        if (ActivityCompat.checkSelfPermission(
                ctx,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.failure()
        }

        val notification = NotificationCompat.Builder(ctx, NotificationUtils.CHANNEL_ID)
            .setContentTitle("Mood Reminder")
            .setContentText("Jangan lupa isi mood kamu hari ini ya ✨")
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(ctx).notify(1001, notification)

        // PENTING — jadwalkan untuk besok
        ReminderScheduler.scheduleDailyReminder(ctx)

        return Result.success()
    }
}

