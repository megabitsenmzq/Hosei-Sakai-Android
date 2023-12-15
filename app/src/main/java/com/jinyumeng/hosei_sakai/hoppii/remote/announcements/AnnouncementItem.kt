package com.jinyumeng.hosei_sakai.hoppii.remote.announcements

data class AnnouncementItem(
    val id: String,
    val title: String,
    val body: String,
    val attachments: List<AttachmentItem>
)
