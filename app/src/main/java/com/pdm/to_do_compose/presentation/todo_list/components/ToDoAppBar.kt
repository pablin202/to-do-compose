package com.pdm.to_do_compose.presentation.todo_list.components

import androidx.compose.material3.ExperimentalMaterial3Api
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
fun DefaultListAppBar() {
    TopAppBar(
        modifier = Modifier.testTag(TestTags.ListScreen.DEFAULT_APP_BAR),
        title = { Text(text = stringResource(id = R.string.tasks)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Preview
@Composable
private fun DefaultListAppBarPreview() {
    DefaultListAppBar()
}

@Preview
@Composable
private fun DefaultListAppBarDarkModePreview() {
    ToDoComposeTheme(darkTheme = true) {
        DefaultListAppBar()
    }
}