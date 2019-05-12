package com.beachball.traincatcher.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.*
import android.support.v4.app.NotificationCompat
import com.beachball.traincatcher.R
import android.app.PendingIntent

class CountdownService : Service() {

    companion object {
        private const val LONG_BUZZ: Long = 750
        private const val SHORT_BUZZ: Long = 250
        private const val WAIT_TIME: Long = 500
        private const val QUICK_BUZZ_NUM = 5
        private const val NOTIFICATION_CODE = 1338
        private const val CHANNEL_ID = "channel_01"
        const val STATION_NAME = "stationName"
        const val TIME_LEFT = "timeLeft"
        private const val WAKE_LOCK_TAG = "TrainCatcher::WakelockTag"
    }

    private val handler = Handler()
    private var secondsLeft = 0
    private var stationName = ""
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        secondsLeft = intent.getIntExtra(TIME_LEFT, 0)
        stationName = intent.getStringExtra(STATION_NAME)
        startCountdownJob(secondsLeft)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        val name = getString(R.string.countdown_channel)
        // Create the channel for the notification
        val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW)

        // Set the Notification Channel for the Notification Manager.
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val t = object : Thread() {
            override fun run() {
                updateNotification(secondsLeft)
            }
        }
        t.start()
        //Acquire wakelock with 20 minute timeout
        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG).apply {
                        acquire(1200000)
                    }
                }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    private fun startCountdownJob(seconds: Int) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        val runnable = Runnable {
            countdownJob(seconds, vibrator)
        }
        handler.postDelayed(runnable, 1000)
    }

    private fun countdownJob(seconds: Int, vibrator: Vibrator?) {
        val newSeconds = seconds - 1
        if(newSeconds % 60 == 0) {
            startVibrationJob(seconds / 60, vibrator)
            updateNotification(seconds / 60)
        }
        if(newSeconds > 0) {
            val runnable = Runnable {
                countdownJob(newSeconds, vibrator)
            }
            handler.postDelayed(runnable, 1000)
        }
    }

    private fun startVibrationJob(minutes: Int, vibrator: Vibrator?) {
        val specialRunnable = Runnable {
            specialVibrationJob(vibrator)
        }
        val runnable = Runnable {
            vibrationJob(vibrator)
        }
        if(minutes == 0) {
            for (i in 1..QUICK_BUZZ_NUM) {
                val timer: Number = (SHORT_BUZZ + WAIT_TIME) * i
                handler.postDelayed(specialRunnable, timer.toLong())
            }
            wakeLock.release()
        } else {
            for (i in 1..minutes) {
                val timer: Number = (LONG_BUZZ + WAIT_TIME) * i
                handler.postDelayed(runnable, timer.toLong())
            }
        }
    }

    private fun specialVibrationJob(vibrator: Vibrator?) {
        vibrator?.vibrate(VibrationEffect.createOneShot(SHORT_BUZZ, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun vibrationJob(vibrator: Vibrator?) {
        vibrator?.vibrate(VibrationEffect.createOneShot(LONG_BUZZ, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun updateNotification(minutes: Int) {
        val intentHide = Intent(this, StopServiceReceiver::class.java)
        val activityPendingIntent = PendingIntent.getBroadcast(this,
                System.currentTimeMillis().toInt(),
                intentHide, 0)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .addAction(android.R.drawable.stat_notify_chat, getString(R.string.countdown_cancel),
                        activityPendingIntent)
                .setContentText(stationName)
                .setContentTitle(String.format(getString(R.string.countdown_str), minutes))
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_train)
                .setVibrate(null)
                .setWhen(System.currentTimeMillis())

        startForeground(NOTIFICATION_CODE, builder.build())
    }
}