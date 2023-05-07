package com.sanryoo.news.feature.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.sanryoo.news.feature.domain.modal.Article
import com.sanryoo.news.feature.presentation._component.CustomBottomNavigation
import com.sanryoo.news.feature.presentation._component.SnackBar
import com.sanryoo.news.feature.presentation.favorite.FavoriteScreen
import com.sanryoo.news.feature.presentation.news.HomeScreen
import com.sanryoo.news.feature.presentation.news_content.NewsContentScreen
import com.sanryoo.news.feature.presentation.search.SearchScreen
import com.sanryoo.news.feature.util.Screen
import com.sanryoo.news.ui.theme.NewsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalMaterialApi
@AndroidEntryPoint
class NewsActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            NewsTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    bottomBar = {
                        CustomBottomNavigation(
                            navController = navController
                        )
                    },
                    scaffoldState = scaffoldState,
                    snackbarHost = { hostState ->
                        SnackbarHost(hostState = hostState) { snackBarData ->
                            SnackBar(snackBarData = snackBarData)
                        }
                    }
                ) {
                    NavHost(
                        navController = navController, startDestination = Screen.Home.route
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(navController, scaffoldState)
                        }
                        composable(Screen.Favorite.route) {
                            FavoriteScreen(navController, scaffoldState)
                        }
                        composable(Screen.Search.route) {
                            SearchScreen(navController, scaffoldState)
                        }
                        composable(
                            route = "${Screen.NewsContent.route}?article={article}&saved={saved}",
                            arguments = listOf(
                                navArgument("article") { type = NavType.StringType },
                                navArgument("saved") { type = NavType.BoolType }
                            )
                        ) { backStackEntry ->
                            val articleJson = backStackEntry.arguments?.getString("article")
                            var article = Article()
                            try {
                                article = Gson().fromJson(articleJson, Article::class.java)
                            } catch (_: Exception) {
                            }
                            val saved = backStackEntry.arguments?.getBoolean("saved") ?: false
                            NewsContentScreen(navController, scaffoldState, article, saved)
                        }
                    }
                }
            }
        }
    }

}