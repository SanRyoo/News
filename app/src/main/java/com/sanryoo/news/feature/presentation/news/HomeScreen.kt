package com.sanryoo.news.feature.presentation.news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.sanryoo.news.R
import com.sanryoo.news.feature.domain.modal.Article
import com.sanryoo.news.feature.presentation._component.NewsItem
import com.sanryoo.news.feature.util.Screen
import com.sanryoo.news.ui.theme.Primary
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.UiEvent.NavigateNewsContent -> {
                    val articleJson = Gson().toJson(event.article)
                    navController.navigate("${Screen.NewsContent.route}?article=${articleJson}&saved=false")
                }
                is HomeViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message, event.label)
                }
            }
        }
    }
    val isLoading = viewModel.loading.collectAsStateWithLifecycle().value
    val isLoadingMore = viewModel.loadingMore.collectAsStateWithLifecycle().value
    val caughtAllNews = viewModel.caughtAllNews.collectAsStateWithLifecycle().value
    val listArticle = viewModel.listArticle.collectAsStateWithLifecycle().value
    HomeContent(
        isLoading = isLoading,
        isLoadingMore = isLoadingMore,
        listArticle = listArticle,
        caughtAllNews = caughtAllNews,
        onRefresh = viewModel::getBreakingNews,
        onLoadMore = viewModel::loadMoreBreakingNews,
        onClickArticle = {
            viewModel.onUiEvent(HomeViewModel.UiEvent.NavigateNewsContent(it))
        },
        saveArticle = viewModel::saveArticle
    )
}

@ExperimentalMaterialApi
@Composable
fun HomeContent(
    isLoading: Boolean = false,
    isLoadingMore: Boolean = false,
    caughtAllNews: Boolean = false,
    listArticle: List<Article> = emptyList(),
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onClickArticle: (Article) -> Unit,
    saveArticle: (Article) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = onRefresh,
        refreshThreshold = 50.dp,
        refreshingOffset = 55.dp,
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), elevation = 10.dp
        ) {
            Text(
                text = "News",
                fontSize = 36.sp,
                fontStyle = FontStyle.Italic,
                color = Primary,
                modifier = Modifier.padding(start = 20.dp),
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState),
            ) {
                items(listArticle.size) { index ->
                    if (index >= listArticle.size - 1 && !isLoadingMore && !isLoading && !caughtAllNews) {
                        onLoadMore()
                    }
                    NewsItem(
                        article = listArticle[index],
                        onClickArticle = onClickArticle,
                        icon = R.drawable.favorite_unselected,
                        onClickIcon = saveArticle,
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 2.dp
                    )
                }
                if (isLoadingMore) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(vertical = 5.dp)
                                    .size(35.dp)
                                    .align(Center),
                                strokeWidth = 4.dp
                            )
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(TopCenter),
                contentColor = Primary,
                backgroundColor = Color.White
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}