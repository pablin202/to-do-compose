package com.pdm.to_do_compose

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.test.platform.app.InstrumentationRegistry
import com.pdm.to_do_compose.presentation.todo_list.ToDoListContent
import com.pdm.to_do_compose.ui.theme.ToDoComposeTheme
import com.pdm.to_do_compose.util.TestTags

class MainActivityTest {

    @get:Rule
    val composableRuleTest = createAndroidComposeRule<MainActivity>()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private var fabButtonText = context.resources.getString(R.string.add_new_task)
    private var appBarText = context.resources.getString(R.string.tasks)

    private fun initCompose() {
        composableRuleTest.activity.setContent {
            ToDoComposeTheme {
                ToDoListContent {

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
        composableRuleTest.onNodeWithTag(TestTags.ListScreen.DEFAULT_APP_BAR, true)
            .assertIsDisplayed()

        composableRuleTest.onNodeWithText(
            appBarText,
            true,
            ignoreCase = true, useUnmergedTree = true
        ).assertIsDisplayed()

    }

    @Test
    fun hideFabButtonTextAfterScroll() {
        initCompose()

        composableRuleTest.onNodeWithText(
            fabButtonText,
            true,
            ignoreCase = true, useUnmergedTree = true
        ).assertIsDisplayed()

        composableRuleTest.onNodeWithTag(TestTags.ListScreen.TASKS_LIST, true)
            .assertIsDisplayed()
            .performTouchInput { swipeUp() }

        composableRuleTest.onNodeWithText(
            fabButtonText,
            true,
            ignoreCase = true, useUnmergedTree = true
        ).assertDoesNotExist()

    }

}