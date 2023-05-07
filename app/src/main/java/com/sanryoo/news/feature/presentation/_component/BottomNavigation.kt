package com.sanryoo.news.feature.presentation._component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sanryoo.news.R
import com.sanryoo.news.feature.util.Screen
import com.sanryoo.news.ui.theme.Primary

@Composable
fun CustomBottomNavigation(navController: NavHostController) {
    val listBottomNavItem = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorite,
        BottomNavItem.Search
    )
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showBottomNav = listBottomNavItem.any { it.route == currentRoute }
    if (showBottomNav) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.White)
        ) {
            for (item in listBottomNavItem) {
                val selected = currentRoute == item.route
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clickable {
                            if (!selected) {
                                navController.navigate(item.route) {
                                    popUpTo(Screen.Home.route)
                                    launchSingleTop = true
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = if (selected) item.selected else item.unselected),
                        contentDescription = "Bottom icon",
                        tint = Primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

sealed class BottomNavItem(
    val route: String,
    val selected: Int,
    val unselected: Int
) {
    object Home : BottomNavItem(
        route = Screen.Home.route,
        selected = R.drawable.home_selected,
        unselected = R.drawable.home_unselected
    )

    object Favorite : BottomNavItem(
        route = Screen.Favorite.route,
        selected = R.drawable.favorite_selected,
        unselected = R.drawable.favorite_unselected
    )

    object Search : BottomNavItem(
        route = Screen.Search.route,
        selected = R.drawable.search_selected,
        unselected = R.drawable.search_unselected
    )
}