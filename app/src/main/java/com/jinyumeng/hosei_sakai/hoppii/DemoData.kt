package com.jinyumeng.hosei_sakai.hoppii

import com.jinyumeng.hosei_sakai.hoppii.remote.assignments.AssignmentItem
import com.jinyumeng.hosei_sakai.hoppii.remote.assignments.Assignments
import com.jinyumeng.hosei_sakai.hoppii.remote.user_info.UserInfo
import java.util.Date
import java.util.UUID

object DemoData {
    val demoUser = UserInfo(
        displayId = "11N4514",
        displayName = "学生太郎",
        eid = "",
        email = "student@hosei.ac.jp",
        firstName = "",
        id = "11N4514",
        lastName = "",
        reference = "",
        entityReference = "",
        entityURL = "",
        entityId = "",
        entityTitle = ""
    )

    var demoAssignments = listOf(
        AssignmentItem(
            author = "",
            authorName = "先生太郎",
            context = "",
            dueTimeString = Date(),
            id = UUID.randomUUID().toString(),
            instructions = "みなさん、よいお年を！",
            status = "",
            submissionType = "",
            title = "課題1",
            markAsFinished = false
        ),
        AssignmentItem(
            author = "",
            authorName = "先生太郎",
            context = "",
            dueTimeString = Date(),
            id = UUID.randomUUID().toString(),
            instructions = "みなさん、よいお年を！",
            status = "",
            submissionType = "",
            title = "課題2",
            markAsFinished = false
        )
    )
}