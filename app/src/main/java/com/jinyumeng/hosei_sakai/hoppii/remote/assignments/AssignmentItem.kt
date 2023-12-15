package com.jinyumeng.hosei_sakai.hoppii.remote.assignments

import java.util.Date

data class AssignmentItem(
    val author: String, // Teacher Name
    var authorName: String?, // Additional
//    val closeTimeString: Date,
    val context: String,
    val dueTimeString: Date,
    val id: String, // !!!
    val instructions: String, // !!!
    val status: String?, // !!!
    val submissionType: String,
    val title: String, // !!!
    var markAsFinished: Boolean = false // Additional
)
