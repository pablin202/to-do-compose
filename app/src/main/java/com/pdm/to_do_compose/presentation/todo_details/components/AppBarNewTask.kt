package com.pdm.to_do_compose.presentation.todo_details.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pdm.to_do_compose.R
import com.pdm.to_do_compose.ui.theme.ToDoComposeTheme
import com.pdm.to_do_compose.util.TestTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarNewTask(
    onBackClicked: () -> Unit,
    onSaveClicked: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.testTag(TestTags.TaskDetailsScreen.TASK_DETAILS_APP_BAR),
        title = { Text(text = stringResource(id = R.string.new_task)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            BackAction {
                onBackClicked()
            }
        },
        actions = {
            SaveAction {
                onSaveClicked()
            }
        },
    )
}

@Composable
fun SaveAction(
    onSaveClicked: () -> Unit
) {
    IconButton(
        onClick = { onSaveClicked() },
        modifier = Modifier.testTag(TestTags.TaskDetailsScreen.SAVE_BUTTON_ACTION)
    ) {
        Icon(
            imageVector = Icons.Filled.Save,
            contentDescription = stringResource(id = R.string.ok_icon),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun BackAction(
    onBackClicked: () -> Unit
) {
    IconButton(
        onClick = { onBackClicked() },
        modifier = Modifier.testTag(TestTags.TaskDetailsScreen.BACK_BUTTON_ACTION)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.arrow_back),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
fun AppBarNewTaskPreview() {
    ToDoComposeTheme {
        AppBarNewTask(onBackClicked = { /*TODO*/ }) {

        }
    }
}

@Preview
@Composable
fun AppBarNewTaskDarkModePreview() {
    ToDoComposeTheme(darkTheme = true) {
        AppBarNewTask(onBackClicked = { /*TODO*/ }) {

        }
    }
}
