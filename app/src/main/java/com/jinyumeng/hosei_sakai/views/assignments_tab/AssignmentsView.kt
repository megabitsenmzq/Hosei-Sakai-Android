package com.jinyumeng.hosei_sakai.views.assignments_tab

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jinyumeng.hosei_sakai.hoppii.AssignmentsManager
import com.jinyumeng.hosei_sakai.ui.theme.HoseiSakaiTheme
import com.jinyumeng.hosei_sakai.views.assignments_tab.detail.AssignmentDetailView
import com.jinyumeng.hosei_sakai.views.assignments_tab.list.AssignmentListView
import com.jinyumeng.hosei_sakai.views.common.LoadingAnimationView

@Composable
fun AssignmentsView() {

    LaunchedEffect(Unit) {
        AssignmentsManager.refreshAssignments()
    }

    val idOfList = "assignment_list"
    val navController = rememberNavController()
    val assignmentList = AssignmentsManager.assignments
    if (assignmentList != null) {

        NavHost(
            navController = navController,
            startDestination = idOfList
        ) {
            composable(idOfList) {
                AssignmentListView(assignmentList = assignmentList, navController = navController)
            }
            composable("assignment_detail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val assignment = assignmentList.find { it.id == id }
                if (assignment != null) {
                    AssignmentDetailView(assignment = assignment, navController = navController)
                } else {
                    Text(text = "Error")
                }
            }
        }
    } else {
        LoadingAnimationView()
    }
}

@Preview(showBackground = true)
@Composable
fun AssignmentsViewPreview() {
    HoseiSakaiTheme {
        AssignmentsView()
    }
}