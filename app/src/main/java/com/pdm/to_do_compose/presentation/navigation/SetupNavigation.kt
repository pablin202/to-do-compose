package com.pdm.to_do_compose.presentation.navigation

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pdm.to_do_compose.presentation.todo_list.ToDoListScreen
import com.pdm.to_do_compose.presentation.todo_details.TaskScreen
import com.pdm.to_do_compose.util.Constants.LIST_ARG_KEY
import com.pdm.to_do_compose.util.Constants.LIST_SCREEN
import com.pdm.to_do_compose.util.Constants.TASK_ARG_KEY
import com.pdm.to_do_compose.util.Constants.TASK_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController
) {
    val screen = remember(navController) {
        Screens(navController = navController)
    }

    NavHost(navController = navController, startDestination = LIST_SCREEN) {
        composable(
            LIST_SCREEN, arguments = listOf(navArgument(LIST_ARG_KEY) {
                type = NavType.StringType
            }),
            enterTransition = {
                scaleIntoContainer()
            },
            exitTransition = {
                scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
            },
            popEnterTransition = {
                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
            },
            popExitTransition = {
                scaleOutOfContainer()
            }
        ) {
            ToDoListScreen {
                screen.task(it)
                Log.d("TaskId", it.toString())
            }
        }

        composable(TASK_SCREEN, arguments = listOf(navArgument(TASK_ARG_KEY) {
            type = NavType.IntType
        }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            TaskScreen {
                screen.list(it)
            }
        }
    }
}