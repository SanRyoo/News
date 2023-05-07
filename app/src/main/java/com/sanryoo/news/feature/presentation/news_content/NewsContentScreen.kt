package com.sanryoo.news.feature.presentation.news_content

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sanryoo.news.R
import com.sanryoo.news.feature.domain.modal.Article
import com.sanryoo.news.ui.theme.Primary
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewsContentScreen(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    article: Article,
    saved: Boolean = false,
    viewModel: NewsContentViewModel = hiltViewModel()
) {
    var showFavoriteIcon by remember {
        mutableStateOf(!saved)
    }
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is NewsContentViewModel.UiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
                is NewsContentViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                IconButton(
                    onClick = {
                        viewModel.onUiEvent(NewsContentViewModel.UiEvent.NavigateBack)
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Icon back",
                        modifier = Modifier
                            .size(35.dp)
                            .padding(horizontal = 10.dp)
                    )
                }
                Text(
                    text = "Explore",
                    color = Primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        floatingActionButton = {
            if (showFavoriteIcon) {
                Card(
                    onClick = {
                        viewModel.saveArticle(article)
                        showFavoriteIcon = false
                    },
                    elevation = 10.dp,
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.favorite_unselected),
                        contentDescription = "Favorite icon",
                        tint = Color.Red,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(35.dp)
                    )
                }
            }
        }
    ) {
        AndroidView(
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    loadUrl(article.url)
                }
            },
            update = {
                it.loadUrl(article.url)
            }
        )
    }
}