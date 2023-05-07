package com.sanryoo.news.feature.util

sealed class Screen(open val route: String) {

    object Home : Screen(route = "home")
    object Favorite : Screen(route = "favorite")
    object Search : Screen(route = "search")
    object NewsContent : Screen(route = "news_content")

}
