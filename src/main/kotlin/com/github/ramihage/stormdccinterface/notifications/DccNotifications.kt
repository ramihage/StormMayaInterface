package com.github.ramihage.stormdccinterface.notifications

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType

import com.github.ramihage.stormdccinterface.MyBundle
import com.github.ramihage.stormdccinterface.toolWindow.MyToolWindowFactory

private const val displayGroup = "Storm DCC Interface Plugin"
private const val titleText = "Storm DCC Interface Plugin"

object DccNotifications {
    fun createConnectionRefusedNotification(): Notification {
        return Notification(
            displayGroup, 
            titleText,
            MyBundle.message(
                "stormdccinterface.notifications.ConnectionRefused",
                MyToolWindowFactory.StormDccInterfaceWindow.currentDcc
            ), 
            NotificationType.ERROR
        )
    }
}