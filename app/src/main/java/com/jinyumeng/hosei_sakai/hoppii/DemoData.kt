package com.jinyumeng.hosei_sakai.hoppii

import com.jinyumeng.hosei_sakai.hoppii.remote.announcements.AnnouncementItem
import com.jinyumeng.hosei_sakai.hoppii.remote.announcements.Announcements
import com.jinyumeng.hosei_sakai.hoppii.remote.assignments.AssignmentItem
import com.jinyumeng.hosei_sakai.hoppii.remote.assignments.Assignments
import com.jinyumeng.hosei_sakai.hoppii.remote.contents.ContentItem
import com.jinyumeng.hosei_sakai.hoppii.remote.contents.Contents
import com.jinyumeng.hosei_sakai.hoppii.remote.sites.SiteItem
import com.jinyumeng.hosei_sakai.hoppii.remote.user_info.UserInfo
import java.util.Calendar
import java.util.Date
import java.util.UUID

object DemoData {

    fun addHoursToDate(date: Date, hours: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR, hours)
        return calendar.time
    }

    val demoUser = UserInfo(
        displayId = "11N4514", displayName = "学生太郎", eid = "", email = "student@hosei.ac.jp", firstName = "", id = "11N4514",
        lastName = "", reference = "", entityReference = "", entityURL = "", entityId = "", entityTitle = ""
    )

    var demoAssignments = listOf(
        AssignmentItem(
            author = "", authorName = "先生太郎", context = "", dueTimeString = addHoursToDate(Date(), 24), id = UUID.randomUUID().toString(),
            instructions = "授業中の指示に従って、PDFを作って提出する。", status = "", submissionType = "", title = "仕様書の提出", markAsFinished = false
        ),
        AssignmentItem(
            author = "", authorName = "先生太郎", context = "", dueTimeString = addHoursToDate(Date(), 24), id = UUID.randomUUID().toString(),
            instructions = "授業中の指示に従って、PDFを作って提出する。", status = "", submissionType = "", title = "日報", markAsFinished = false
        ),
        AssignmentItem(
            author = "", authorName = "先生太郎", context = "", dueTimeString = addHoursToDate(Date(), 24), id = UUID.randomUUID().toString(),
            instructions = "授業中の指示に従って、PDFを作って提出する。", status = "", submissionType = "", title = "作品タイトルの確認", markAsFinished = false
        ),
        AssignmentItem(
            author = "", authorName = "先生太郎", context = "", dueTimeString = addHoursToDate(Date(), 24), id = UUID.randomUUID().toString(),
            instructions = "授業中の指示に従って、PDFを作って提出する。", status = "", submissionType = "", title = "相互評価シート", markAsFinished = false
        ),
        AssignmentItem(
            author = "", authorName = "先生太郎", context = "", dueTimeString = addHoursToDate(Date(), 24), id = UUID.randomUUID().toString(),
            instructions = "授業中の指示に従って、PDFを作って提出する。", status = "", submissionType = "", title = "レポート 1", markAsFinished = false
        )
    )

    val demoSites = listOf(
        SiteItem(id = UUID.randomUUID().toString(), title = "プロダクト理論", joinable = true, entityId = "", entityTitle = ""),
        SiteItem(id = UUID.randomUUID().toString(), title = "ゼミナール 3", joinable = true, entityId = "", entityTitle = ""),
        SiteItem(id = UUID.randomUUID().toString(), title = "映像制作", joinable = true, entityId = "", entityTitle = ""),
        SiteItem(id = UUID.randomUUID().toString(), title = "マイコン基礎", joinable = true, entityId = "", entityTitle = ""),
        SiteItem(id = UUID.randomUUID().toString(), title = "フランス語", joinable = true, entityId = "", entityTitle = "")
    )

    val demoAnnouncements = listOf(
        AnnouncementItem(attachments = listOf(), body = "皆さん！よいお年を！", id = UUID.randomUUID().toString(), title = "最終発表会と提出物に関して"),
        AnnouncementItem(attachments = listOf(), body = "皆さん！よいお年を！", id = UUID.randomUUID().toString(), title = "物品購入について"),
        AnnouncementItem(attachments = listOf(), body = "皆さん！よいお年を！", id = UUID.randomUUID().toString(), title = "最終成果物"),
        AnnouncementItem(attachments = listOf(), body = "皆さん！よいお年を！", id = UUID.randomUUID().toString(), title = "1月15日の教室变更案内"),
        AnnouncementItem(attachments = listOf(), body = "皆さん！よいお年を！", id = UUID.randomUUID().toString(), title = "パネルガイダンス")
    )

    val demoContents = listOf(
        ContentItem(title = "Empty", type = "Header", url = "about:blank"),
        ContentItem(title = "グループ分け.pdf", type = "PDF", url = "about:blank"),
        ContentItem(title = "提出スケジュール.pdf", type = "PDF", url = "about:blank")
    )

    val demoTimetable = listOf(
        listOf("", "", "", "", "", "", ""),
        listOf("", "", "プロダクト理論", "プロダクト理論", "", "", ""),
        listOf("", "", "", "ゼミナール 3", "ゼミナール 3", "", ""),
        listOf("", "", "プロダクト理論", "プロダクト理論", "映像制作", "", ""),
        listOf("", "フランス語", "マイコン基礎", "マイコン基礎", "", "", ""),
        listOf("", "", "", "", "", "", ""),
        listOf("", "", "", "", "", "", "")
    )
}