package com.sami.tradingchallengetracker.util

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.google.firebase.firestore.ListenerRegistration
import com.sami.tradingchallengetracker.ui.CommunityCloudManager

class CommunityNotificationService : Service() {

    private var listenerRegistration: ListenerRegistration? = null
    private var currentActiveUserId: Int = 0

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val prefs = getSharedPreferences("sami_auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getInt("user_id", 0)
        val sessionActive = prefs.getBoolean("session_active", false)

        if (sessionActive && userId > 0) {
            if (listenerRegistration == null || currentActiveUserId != userId) {
                listenerRegistration?.remove()
                currentActiveUserId = userId
                listenerRegistration = CommunityCloudManager.subscribeToNotificationsForUser(
                    context = applicationContext,
                    currentUserId = userId
                ) { _ -> }
            }
        } else {
            listenerRegistration?.remove()
            listenerRegistration = null
            stopSelf()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        listenerRegistration?.remove()
        listenerRegistration = null
        super.onDestroy()
    }
}
