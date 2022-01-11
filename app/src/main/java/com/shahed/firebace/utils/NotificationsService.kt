package com.shahed.firebace.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.shahed.firebace.R
import com.shahed.firebace.views.MainActivity

class NotificationsService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "From: " + remoteMessage.from)

        if (remoteMessage.data.isNotEmpty()) {

            Log.d(TAG, "Message data payload: " + remoteMessage.data)

        }

        //  int order_id=Integer.parseInt(Objects.requireNonNull(remoteMessage.getData().get("order_id")));
        if (remoteMessage.notification != null) {

            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification?.body)


            val mPrefs = applicationContext.getSharedPreferences(
                AppConstants.PREF_NAME,
                Context.MODE_PRIVATE
            )
            mPrefs.edit().putBoolean("PREF_KEY_NEW_RESERVATIONS", true).apply()


            notify(
                remoteMessage.notification?.body,
                remoteMessage.notification?.title
            )

        }
    }

    private fun notify(body: String?, title: String?) {

        val requestID = System.currentTimeMillis().toInt()
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("fcm", true)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

//        Uri uri = Uri.parse(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
//        intent.setDataAndType(uri, "*/*");
//        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        val stackBuilder = TaskStackBuilder.create(applicationContext)
        //        stackBuilder.addParentStack(HomeActivity.class);
//        stackBuilder.addNextIntentWithParentStack(intent)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            requestID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
        val bigText: NotificationCompat.BigTextStyle = NotificationCompat.BigTextStyle()
        //bigText.bigText("test");
        bigText.setBigContentTitle(title)
        bigText.setSummaryText(body)
//        mBuilder.setSound(alarmSound)
        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
        mBuilder.setContentTitle(title)
        mBuilder.setContentText(body)
        mBuilder.priority = Notification.PRIORITY_HIGH
        mBuilder.setStyle(bigText)
        mBuilder.setContentIntent(pendingIntent)


        val mNotificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId: String = getString(R.string.default_notification_channel_id)
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
        mNotificationManager.notify(requestID, mBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val mPrefs =
            applicationContext.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE)
        mPrefs.edit().putString("PREF_KEY_TOKEN", token).apply()
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun startService(service: Intent): ComponentName? {
        return super.startService(service)
    }

    override fun startForegroundService(service: Intent): ComponentName? {
        return super.startForegroundService(service)
    }

    override fun stopService(name: Intent): Boolean {
        return super.stopService(name)
    }

    companion object {
        private const val TAG = "NotificationService"
        fun start(context: Context) {
            val starter = Intent(context, NotificationsService::class.java)
            context.startService(starter)
        }
    }
}