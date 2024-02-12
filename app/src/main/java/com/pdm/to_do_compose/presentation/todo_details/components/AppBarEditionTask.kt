package com.pdm.to_do_compose.presentation.todo_details.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
fun AppBarEditionTask(
    onBackClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onSaveClicked: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.testTag(TestTags.TaskDetailsScreen.TASK_DETAILS_APP_BAR),
        title = { Text(text = stringResource(id = R.string.task)) },
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
            DeleteAction {
                onDeleteClicked()
            }
            SaveAction {
                onSaveClicked()
            }
        },
    )
}

@Composable
fun DeleteAction(
    onDeleteClicked: () -> Unit
) {
    IconButton(
        onClick = { onDeleteClicked() },
        modifier = Modifier.testTag(TestTags.TaskDetailsScreen.DELETE_BUTTON_ACTION)
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_icon),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
fun AppBarEditionTaskPreview() {
    ToDoComposeTheme {
        AppBarEditionTask({ }, { /*TODO*/ }) {

        }
    }
}

@Preview
@Composable
fun AppBarEditionTaskDarkModePreview() {
    ToDoComposeTheme(darkTheme = true) {
        AppBarEditionTask({}, { /*TODO*/ }) {

        }
    }
}
