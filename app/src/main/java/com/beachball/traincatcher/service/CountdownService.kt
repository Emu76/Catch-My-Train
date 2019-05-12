package com.beachball.traincatcher.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.*
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.beachball.traincatcher.R

class CountdownService : Service() {

    companion object {
        private const val LONG_BUZZ: Long = 750
        private const val SHORT_BUZZ: Long = 250
        private const val WAIT_TIME: Long = 500
        private const val QUICK_BUZZ_NUM = 5
    }

    private val handler = Handler()
    private var integer = 0
    private var stationName = ""
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        integer = intent.getIntExtra("timeLeft", 0)
        stationName = intent.getStringExtra("stationName")
        startCountdownJob(integer)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Countdown channel"
            // Create the channel for the notification
            val mChannel = NotificationChannel("channel_01", name, NotificationManager.IMPORTANCE_DEFAULT)

            // Set the Notification Channel for the Notification Manager.
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(mChannel)

            val t = object : Thread() {
                override fun run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForeground(1338,
                                buildForegroundNotification())
                    }
                }
            }
            t.start()
        }
        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TrainCatcher::WakelockTag").apply {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(SHORT_BUZZ, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(SHORT_BUZZ)
        }
    }

    private fun vibrationJob(vibrator: Vibrator?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(LONG_BUZZ, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(LONG_BUZZ)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildForegroundNotification(): Notification {
        // The PendingIntent that leads to a call to onStartCommand() in this service.
        //val servicePendingIntent = PendingIntent.getService(this, 0, intent,
        //        PendingIntent.FLAG_UPDATE_CURRENT)

        // The PendingIntent to launch activity.
        val intentHide = Intent(this, StopServiceReceiver::class.java)
        val activityPendingIntent = PendingIntent.getBroadcast(this,
                System.currentTimeMillis().toInt(),
                intentHide, 0)

        val builder = NotificationCompat.Builder(this)
                .addAction(android.R.drawable.stat_notify_chat, "Cancel countdown",
                        activityPendingIntent)
                .setContentText(stationName)
                .setContentTitle("Your train leaves in 4 minutes")
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_train)
                .setWhen(System.currentTimeMillis())

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("channel_01") // Channel ID
        }

        return builder.build()
    }
}