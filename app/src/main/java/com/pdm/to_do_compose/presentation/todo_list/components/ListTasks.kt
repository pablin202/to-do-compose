package com.pdm.to_do_compose.presentation.todo_list.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.domain.models.ToDoTaskModel
import com.pdm.to_do_compose.ui.theme.LARGE_PADDING
import com.pdm.to_do_compose.ui.theme.PRIORITY_INDICATOR_SIZE
import com.pdm.to_do_compose.ui.theme.ToDoComposeTheme
import com.pdm.to_do_compose.util.TestTags

@Composable
fun ListTasks(
    tasks: List<ToDoTaskModel> = emptyList(),
    innerPadding: PaddingValues,
    listState: LazyListState,
    onTaskSelected: (ToDoTaskModel) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .testTag(TestTags.ListScreen.TASKS_LIST),
    ) {
        items(items = tasks, key = { it.id }) { task ->
            TaskItem(
                toDoTaskModel = task,
                onTaskSelected = onTaskSelected
            )
        }
    }
}

@Composable
fun TaskItem(
    toDoTaskModel: ToDoTaskModel,
    onTaskSelected: (ToDoTaskModel) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        onClick = {
            onTaskSelected(toDoTaskModel)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(all = LARGE_PADDING)
                .fillMaxWidth()
        ) {
            Row() {
                Text(
                    modifier = Modifier.weight(8f),
                    text = toDoTaskModel.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Canvas(
                        modifier = Modifier
                            .width(PRIORITY_INDICATOR_SIZE)
                            .height(
                                PRIORITY_INDICATOR_SIZE
                            ),
                        onDraw = {
                            drawCircle(color = Priority.entries[toDoTaskModel.priority].color)
                        }
                    )
                }
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = toDoTaskModel.description,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun TaskItemPreview() {
    TaskItem(
        ToDoTaskModel(
            1,
            "Title 1",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. ",
            Priority.HIGH.ordinal
        ),
        {},
    )
}

@Preview
@Composable
fun TaskItemDarkModePreview() {
    ToDoComposeTheme(darkTheme = true) {
        TaskItem(
            ToDoTaskModel(
                1,
                "Title 1",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. ",
                Priority.HIGH.ordinal
            ),
            {},
        )
    }
}