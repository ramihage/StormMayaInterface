package com.github.ramihage.stormdccinterface.notifications

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType

import com.github.ramihage.stormdccinterface.MyBundle
import com.github.ramihage.stormdccinterface.toolWindow.MyToolWindowFactory

private const val displayGroup = "StormDCC Interface Plugin"
private const val titleText = "StormDCC Interface Plugin"

object DccNotifications {
    val CONNECTION_REFUSED = Notification(
        displayGroup, titleText,
        MyBundle.message("stormdccinterface.notifications.ConnectionRefused"), NotificationType.ERROR
    )
}