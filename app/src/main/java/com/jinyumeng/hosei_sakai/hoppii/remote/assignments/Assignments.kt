package com.jinyumeng.hosei_sakai.hoppii.remote.assignments

import com.squareup.moshi.Json

data class Assignments(
    @Json(name = "assignment_collection")
    var assignmentCollection: MutableList<AssignmentItem>
)
