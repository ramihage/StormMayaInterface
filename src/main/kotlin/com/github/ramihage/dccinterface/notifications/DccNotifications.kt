package com.github.ramihage.testplugin.notifications

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType

import com.github.ramihage.testplugin.MyBundle

private const val displayGroup = "MayaCharm"
private const val titleText = "MayaCharm"

object DccNotifications {
    val CONNECTION_REFUSED = Notification(
        displayGroup, titleText,
        MyBundle.message("mayacharm.notifications.ConnectionRefused"), NotificationType.ERROR
    )
}