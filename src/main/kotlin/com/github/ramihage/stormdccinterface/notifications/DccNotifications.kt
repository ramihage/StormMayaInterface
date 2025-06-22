package com.github.ramihage.stormdccinterface.notifications

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType

import com.github.ramihage.stormdccinterface.MyBundle

private const val displayGroup = "MayaCharm"
private const val titleText = "MayaCharm"

object DccNotifications {
    val CONNECTION_REFUSED = Notification(
        displayGroup, titleText,
        MyBundle.message("stormdccinterface.notifications.ConnectionRefused"), NotificationType.ERROR
    )
}