/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.eggtimernotifications.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.android.eggtimernotifications.MainActivity
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.receiver.SnoozeReceiver

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0


/**
 * Builds and delivers the notification.
 *
 * @param messageBody Message body
 * @param applicationContext Application context
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    // Create the content intent for the notification, which launches
    // this activity
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        //To update or cancel this pending intent, need to use this code to access the pending
        // intent.
        REQUEST_CODE,
        //The snoozeIntent object which is the intent of the activity to be launched.
        snoozeIntent,
        //The quick action and the notification will disappear after the first tap which is why the
        //intent can only be used once.
        FLAGS
    )

    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.cooked_egg
    )

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        //bigLargeIcon() to null so the large icon goes away when the notification is expanded
        .bigLargeIcon(null)

    val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.egg_notification_channel_id)
        )
        .setSmallIcon(R.drawable.cooked_egg)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setStyle(bigPicStyle)
        //setLargeIcon on the builder so the image will be displayed as a smaller icon when notification is collapsed.
        .setLargeIcon(eggImage)
        // This action will be used to trigger the right broadcastReceiver.
        .addAction(
            R.drawable.egg_icon,
            applicationContext.getString(R.string.snooze),
            snoozePendingIntent
        )

    // TODO: Step 2.5 set priority

    //Directly call notify() since performing the call from an extension function on the
    //same class
    notify(NOTIFICATION_ID, builder.build())

}

fun NotificationManager.cancelNotification() {
    cancelAll()
}
