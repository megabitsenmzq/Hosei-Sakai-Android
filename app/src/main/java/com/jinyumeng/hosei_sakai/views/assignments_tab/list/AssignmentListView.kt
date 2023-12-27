package com.jinyumeng.hosei_sakai.views.assignments_tab.list

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jinyumeng.hosei_sakai.hoppii.AssignmentsManager
import com.jinyumeng.hosei_sakai.hoppii.remote.assignments.AssignmentItem
import com.jinyumeng.hosei_sakai.views.common.ListSectionTitleView


@Composable
private fun DoneSwipeBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            Icons.Default.Done,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 16.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun UndoneSwipeBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            Icons.Default.Close,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 16.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

private enum class AssignmentsListCellType {
    Normal,
    FinishTip,
    UnfinishedHeader,
    FinishedHeader
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AssignmentListView(assignmentList: List<AssignmentItem>, navController: NavController) {

    // Compose list cells.
    val dataSource: MutableList<Pair<AssignmentItem?, AssignmentsListCellType>> = mutableListOf()
    dataSource.add(Pair(null, AssignmentsListCellType.FinishTip))
    dataSource.add(Pair(null, AssignmentsListCellType.UnfinishedHeader))
    dataSource.addAll(assignmentList.filter { !it.markAsFinished }.map { Pair(it, AssignmentsListCellType.Normal) })
    dataSource.add(Pair(null, AssignmentsListCellType.FinishedHeader))
    dataSource.addAll(assignmentList.filter { it.markAsFinished }.map { Pair(it, AssignmentsListCellType.Normal) })

    Scaffold(
        topBar = {
            AssignmentsTopBarView()
        }
    ) { paddingValues ->
        // TODO: Add a "No assignments" view.
        LazyColumn(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
            items(
                items = dataSource,
                key = {
                    when (it.second) {
                        AssignmentsListCellType.Normal -> it.first!!.id
                        AssignmentsListCellType.FinishTip -> "FinishTip"
                        AssignmentsListCellType.UnfinishedHeader -> "UnfinishedHeader"
                        AssignmentsListCellType.FinishedHeader -> "FinishedHeader"
                    }
                }
            ) { item ->
                when (item.second) {
                    AssignmentsListCellType.Normal -> {
                        val assignment = item.first!!
                        val dismissState = rememberDismissState(
                            confirmValueChange = {
                                val newList = assignmentList.toMutableList()
                                newList.find { it.id == assignment.id }?.markAsFinished = !assignment.markAsFinished
                                AssignmentsManager.updateAssignments(newList)
                                AssignmentsManager.savedFinishGestureTipShowed = true
                                Log.d("AssignmentListView", "Mark as finished: ${assignment.title}")
                                return@rememberDismissState false
                            }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            modifier = Modifier.animateItemPlacement(),
                            background = {
                                if (assignment.markAsFinished)
                                    UndoneSwipeBackground() else DoneSwipeBackground()
                            },
                            directions = setOf(DismissDirection.StartToEnd),
                            dismissContent = {
                                AssignmentListRowView(
                                    title = assignment.title,
                                    teacher = assignment.authorName ?: "Error",
                                    dueTime = assignment.dueTimeString,
                                    shouldDim = assignment.markAsFinished,
                                    onClick = {
                                        navController.navigate("assignment_detail/${assignment.id}")
                                    }
                                )
                            }
                        )

                    }
                    AssignmentsListCellType.FinishTip -> {
                        if (!AssignmentsManager.finishGestureTipShowed) {
                            val dismissState = rememberDismissState(
                                confirmValueChange = {
                                    AssignmentsManager.savedFinishGestureTipShowed = true
                                    return@rememberDismissState true
                                }
                            )
                            SwipeToDismiss(
                                state = dismissState,
                                modifier = Modifier.animateItemPlacement(),
                                background = { DoneSwipeBackground() },
                                directions = setOf(DismissDirection.StartToEnd),
                                dismissContent = {
                                    FinishTipView()
                                }
                            )
                        }

                    }
                    AssignmentsListCellType.UnfinishedHeader -> {
                        ListSectionTitleView(title = "進行中")
                    }
                    AssignmentsListCellType.FinishedHeader -> {
                        ListSectionTitleView(title = "完了")
                    }
                }
            }
        }
    }
}