package com.jinyumeng.hosei_sakai.views.timetable_tab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jinyumeng.hosei_sakai.BuildConfig
import com.jinyumeng.hosei_sakai.ui.theme.HoseiSakaiTheme
import com.jinyumeng.hosei_sakai.views.login.LoginInterfaceView

@Composable
fun TimetableContentView(
    noSunday: Boolean,
    noSaturday: Boolean,
    table: List<List<String>>
) {
    val lineGray = Color(0xFFE5E5EA)
    @Composable
    fun VHeader(
        noSunday: Boolean,
        noSaturday: Boolean
    ) {
        val headerText = mutableListOf("日", "月", "火", "水", "木", "金", "土")
        if (noSunday) {
            headerText.removeFirst()
        }
        if (noSaturday) {
            headerText.removeLast()
        }

        Row {
            for (text in headerText) {
                Box(modifier = Modifier
                    .weight(1f)
                    .height(20.dp)
                    .border(0.5.dp, lineGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = text, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    @Composable
    fun HHeader(size: Int) {
        Column {
            Box(modifier = Modifier
                .width(20.dp)
                .height(20.dp)
                .border(0.5.dp, lineGray),
            ) {}
            for (i in 1..size) {
                Box(modifier = Modifier
                    .weight(1f)
                    .width(20.dp)
                    .border(0.5.dp, lineGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "$i", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    @Composable
    fun TableBody(table: List<List<String>>) {
        Column {
            for (row in table) {
                Box(modifier = Modifier.weight(1f)) {
                    Row {
                        for (cell in row) {
                            if (cell == "") {
                                Box(modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                                    .border(0.5.dp, lineGray)
                                ) {}
                                continue
                            }
                            Box(modifier = Modifier
                                .weight(1f)
                                .border(0.5.dp, lineGray)
                                .padding(1.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(3.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(text = cell, color = MaterialTheme.colorScheme.onPrimary, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    val vSize = table.first().size
    Row(modifier = Modifier.border(0.5.dp, Color.LightGray)) {
        HHeader(size = vSize)
        Column() {
            VHeader(noSunday = noSunday, noSaturday = noSaturday)
            Box(modifier = Modifier.weight(1f)) {
                TableBody(table = table)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimetableContentViewPreview() {
    Surface {
        TimetableContentView(
            noSunday = false,
            noSaturday = false,
            table = listOf(
                listOf("", "", "", "", "", "", ""),
                listOf("", "", "", "", "情報システムデザイン", "", ""),
                listOf(
                    "",
                    "プロジェクト実習・制作２",
                    "",
                    "プロジェクト実習・制作２",
                    "ＡＩプログラミング",
                    "",
                    ""
                ),
                listOf(
                    "",
                    "プロジェクト実習・制作２",
                    "デザイン・バックキャスティン...",
                    "プロジェクト実習・制作２",
                    "ＡＩプログラミング",
                    "",
                    ""
                ),
                listOf(
                    "",
                    "",
                    "デザイン・バックキャスティン...",
                    "デザインケーススタディ",
                    "",
                    "",
                    ""
                ),
                listOf("", "", "", "", "", "", ""),
                listOf("", "", "", "", "", "", "")
            )
        )
    }
}