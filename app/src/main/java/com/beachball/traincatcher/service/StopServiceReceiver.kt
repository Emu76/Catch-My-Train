package com.beachball.traincatcher.service

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context


class StopServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val service = Intent(context, CountdownService::class.java)
        context.stopService(service)
    }
}