package com.neighbor.neighborsrefrigerator.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.UserData

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        /* 쉐어드 프리퍼런스로 유저데이터 초기화 후 fcm token 업데이트
        val userData = UserData()
        val dbAccess = DBAccessModule()
        dbAccess.updateFcmToken(userData);
         */

        Log.d("new Token", token)
    }

    // 메세지 받을때 호출
    override fun onMessageReceived(remoteMessage: RemoteMessage) { // 메세지 수신
        if(remoteMessage.data.isNotEmpty()){
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body
            val data = remoteMessage.data

            if(title != null && body != null){
                Log.d(TAG, "sendNotification: title: $title, body: $body, data: $data")
                sendNotification(title, body, data)
            }
        }
    }

    private fun sendNotification(title: String?, body: String?, data: Map<String, String>) {

        // 알림 채널 이름
        val channelId = getString(R.string.default_notification_channel_id)

        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // 아이콘 설정
            .setContentTitle(title) // 제목
            .setContentText(body) // 메시지 내용
            .setAutoCancel(true)
            .setSound(soundUri) // 알림 소리
            //.setContentIntent() // 알림 실행 시 Intent

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 이후에는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        notificationManager.notify(0, notificationBuilder.build())
    }
}
