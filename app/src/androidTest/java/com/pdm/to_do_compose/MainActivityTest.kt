package com.pdm.to_do_compose

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.test.platform.app.InstrumentationRegistry
import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.presentation.todo_list.ToDoListContent
import com.pdm.to_do_compose.presentation.todo_list.ToDoListScreen
import com.pdm.to_do_compose.presentation.todo_list.ToDoListViewModel
import com.pdm.to_do_compose.ui.theme.ToDoComposeTheme
import com.pdm.to_do_compose.util.SearchAppBarState
import com.pdm.to_do_compose.util.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import org.junit.Before
import javax.inject.Inject

@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composableRuleTest = createAndroidComposeRule<MainActivity>()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private var fabButtonText = context.resources.getString(R.string.add_new_task)
    private var appBarText = context.resources.getString(R.string.tasks)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    private fun initCompose() {
        composableRuleTest.activity.setContent {
            ToDoComposeTheme {
                ToDoListScreen() {

                }
            }
        }
    }

    @Test
    fun assertAddTaskButtonIsDisplayed() {
        initCompose()
        composableRuleTest.onNodeWithTag(TestTags.ListScreen.FAB_BUTTON, true)
            .assertIsDisplayed()
    }

    @Test
    fun assertDefaultAppBarIsDisplayed() {
        initCompose()

        with(composableRuleTest) {
            onNodeWithTag(TestTags.ListScreen.DEFAULT_APP_BAR, true)
                .assertIsDisplayed()

            onNodeWithText(
                appBarText,
                true,
                ignoreCase = true, useUnmergedTree = true
            ).assertIsDisplayed()

            onNodeWithTag(
                TestTags.ListScreen.SEARCH_BUTTON_ACTION, true
            )
                .assertIsDisplayed()

            onNodeWithTag(
                TestTags.ListScreen.SORT_BUTTON_ACTION, true
            )
                .assertIsDisplayed()

            onNodeWithTag(
                TestTags.ListScreen.DELETE_ALL_BUTTON_ACTION, true
            )
                .assertIsDisplayed()
        }
    }

    @Test
    fun hideFabButtonTextAfterScroll() {
        initCompose()

        with(composableRuleTest) {
            onNodeWithText(
                fabButtonText,
                true,
                ignoreCase = true, useUnmergedTree = true
            ).assertIsDisplayed()

            onNodeWithTag(TestTags.ListScreen.TASKS_LIST, true)
                .assertIsDisplayed()
                .performTouchInput { swipeUp() }

            onNodeWithText(
                fabButtonText,
                true,
                ignoreCase = true, useUnmergedTree = true
            ).assertDoesNotExist()
        }
    }

    @Test
    fun assertDropDownMenuVisibility() {
        initCompose()

        with(composableRuleTest) {
            onNodeWithTag(
                TestTags.ListScreen.SORT_BUTTON_ACTION, true
            )
                .assertIsDisplayed().performClick()

            onNodeWithText(
                Priority.LOW.name,
                true,
                ignoreCase = false, useUnmergedTree = true
            ).assertIsDisplayed()

            onNodeWithText(
                Priority.HIGH.name,
                true,
                ignoreCase = false, useUnmergedTree = true
            ).assertIsDisplayed()
        }
    }

    @Test
    fun assertSearchVisibilityIsShown() {
        initCompose()

        with(composableRuleTest) {
            onNodeWithTag(
                TestTags.ListScreen.SEARCH_BUTTON_ACTION, true
            )
                .assertIsDisplayed().performClick()

            onNodeWithTag(
                TestTags.ListScreen.SEARCH_APP_BAR,
                useUnmergedTree = true
            ).assertIsDisplayed()
        }
    }
}