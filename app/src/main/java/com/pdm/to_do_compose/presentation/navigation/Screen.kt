package com.pdm.to_do_compose.presentation.navigation

import androidx.navigation.NavHostController
import com.pdm.to_do_compose.util.Action
import com.pdm.to_do_compose.util.Constants.LIST_SCREEN

class Screens (navController: NavHostController) {
    val list: (Action) -> Unit = {action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) {inclusive = true}
        }
    }

    val task: (Int) -> Unit = { id ->
        navController.navigate("task/$id")
    }

}