package com.sami.tradingchallengetracker.util

import com.sami.tradingchallengetracker.ScreenTab

object ActiveConversationTracker {
    @Volatile
    var isAppInForeground: Boolean = false

    @Volatile
    var activeTab: ScreenTab = ScreenTab.DASHBOARD

    @Volatile
    var isPrivateChatOpen: Boolean = false

    @Volatile
    var activeChannelId: String? = null

    @Volatile
    var activePrivateUserId: Int? = null

    /**
     * Determines whether the user is actively viewing this exact conversation right now.
     * If true, system notifications are suppressed to prevent noisy duplicate popups.
     */
    fun isUserActivelyViewing(channelId: String?, isPrivate: Boolean, privateUserId: Int?): Boolean {
        if (!isAppInForeground) return false
        if (isPrivate) {
            val inPrivate = activeTab == ScreenTab.PRIVATE_CHAT || (activeTab == ScreenTab.CHAT && isPrivateChatOpen)
            if (!inPrivate) return false
            return if (activePrivateUserId != null && privateUserId != null) {
                activePrivateUserId == privateUserId
            } else {
                privateUserId == 1 || privateUserId == null
            }
        } else {
            return activeTab == ScreenTab.CHAT && !isPrivateChatOpen && activeChannelId != null && activeChannelId == channelId
        }
    }
}
