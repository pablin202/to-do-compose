package com.pdm.to_do_compose.presentation.todo_list.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import com.pdm.to_do_compose.R
import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.ui.theme.LARGE_PADDING
import com.pdm.to_do_compose.ui.theme.PRIORITY_INDICATOR_SIZE
import com.pdm.to_do_compose.ui.theme.TOP_APP_BAR_HEIGHT
import com.pdm.to_do_compose.ui.theme.ToDoComposeTheme
import com.pdm.to_do_compose.ui.theme.Typography
import com.pdm.to_do_compose.util.TestTags
import com.pdm.to_do_compose.util.TestTags.ListScreen.CLOSE_BUTTON_ACTION
import com.pdm.to_do_compose.util.TestTags.ListScreen.DELETE_ALL_BUTTON_ACTION
import com.pdm.to_do_compose.util.TestTags.ListScreen.SEARCH_APP_BAR
import com.pdm.to_do_compose.util.TestTags.ListScreen.SEARCH_BUTTON_ACTION
import com.pdm.to_do_compose.util.TestTags.ListScreen.SEARCH_TEXT_INPUT
import com.pdm.to_do_compose.util.TestTags.ListScreen.SORT_BUTTON_ACTION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onOpenSearchBarClicked: () -> Unit,
    onPriorityChanged: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.testTag(TestTags.ListScreen.DEFAULT_APP_BAR),
        title = { Text(text = stringResource(id = R.string.tasks)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            ListAppBarActions({ onOpenSearchBarClicked() }, { onPriorityChanged(it) }) {
                onDeleteAllClicked()
            }
        }
    )
}

@Composable
fun ListAppBarActions(
    onOpenSearchBarClicked: () -> Unit,
    onPriorityChanged: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    SearchAction(onOpenSearchBarClicked)
    SortAction(onPriorityChanged)
    DeleteAllAction(onDeleteAllClicked)
}

@Composable
fun SearchAction(
    onOpenSearchBarClicked: () -> Unit
) {
    IconButton(
        onClick = { onOpenSearchBarClicked() },
        modifier = Modifier.testTag(SEARCH_BUTTON_ACTION)
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_tasks),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllClicked: () -> Unit
) {
    IconButton(
        onClick = { onDeleteAllClicked() },
        modifier = Modifier.testTag(DELETE_ALL_BUTTON_ACTION)
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_all),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun SortAction(
    onPriorityChanged: (Priority) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(
        onClick = { expanded = true },
        modifier = Modifier.testTag(SORT_BUTTON_ACTION)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = stringResource(id = R.string.sort_tasks),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {

            DropdownMenuItem(text = { PriorityItem(priority = Priority.LOW) }, onClick = {
                expanded = false
                onPriorityChanged(Priority.LOW)
            })

            DropdownMenuItem(text = { PriorityItem(priority = Priority.HIGH) }, onClick = {
                expanded = false
                onPriorityChanged(Priority.HIGH)
            })

            DropdownMenuItem(text = { PriorityItem(priority = Priority.NONE) }, onClick = {
                expanded = false
                onPriorityChanged(Priority.NONE)
            })
        }
    }
}

@Composable
fun PriorityItem(
    priority: Priority
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)) {
            drawCircle(color = priority.color)
        }
        Text(
            modifier = Modifier.padding(start = LARGE_PADDING),
            text = priority.name,
            style = Typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
//    onSearchClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT)
            .testTag(SEARCH_APP_BAR),
        color = MaterialTheme.colorScheme.primary
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(SEARCH_TEXT_INPUT),
            value = text,
            onValueChange = { onTextChange(it) },
            placeholder = {
                Text(
                    fontWeight = FontWeight.Normal,
                    text = stringResource(id = R.string.search),
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier.alpha(alpha = 0.38f),
                    onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_icon),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = { onCloseClicked() },
                    modifier = Modifier.testTag(CLOSE_BUTTON_ACTION)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close_icon),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.None
            ),
//            keyboardActions = KeyboardActions(
//                onSearch = {
//                    onSearchClicked()
//                }
//            ),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun DefaultListAppBarPreview() {
    DefaultListAppBar({}, {}) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun DefaultListAppBarDarkModePreview() {
    ToDoComposeTheme(darkTheme = true) {
        DefaultListAppBar({}, {}) {

        }
    }
}

@Preview
@Composable
private fun SearchAppBarPreview() {
    SearchAppBar("", {}) {

    }
}

@Preview
@Composable
private fun SearchAppBarDarkModPreview() {
    ToDoComposeTheme(darkTheme = true) {
        SearchAppBar("", {}) {

        }
    }
}

@Preview
@Composable
fun HighPriorityItemPreview() {
    PriorityItem(priority = Priority.HIGH)
}

@Preview
@Composable
fun MediumPriorityItemPreview() {
    PriorityItem(priority = Priority.MEDIUM)
}

@Preview
@Composable
fun LowPriorityItemPreview() {
    PriorityItem(priority = Priority.LOW)
}

@Preview
@Composable
fun NonePriorityItemPreview() {
    PriorityItem(priority = Priority.NONE)
}