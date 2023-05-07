package com.sanryoo.news.feature.presentation.favorite

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
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
fun FavoriteScreen(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is FavoriteViewModel.UiEvent.NavigateNewsContent -> {
                    val articleJson = Gson().toJson(event.article)
                    navController.navigate("${Screen.NewsContent.route}?article=${articleJson}&saved=true")
                }
                is FavoriteViewModel.UiEvent.ShowSnackBar -> {
                    val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(event.message, event.label)
                    when (snackBarResult) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {
                            viewModel.undo()
                        }
                    }
                }
            }
        }
    }
    val listArticleSaved = viewModel.listArticleSaved.collectAsStateWithLifecycle().value
    FavoriteContent(
        listArticleSaved = listArticleSaved,
        onDelete = viewModel::deleteArticle,
        onClickArticle = {
            viewModel.onUiEvent(FavoriteViewModel.UiEvent.NavigateNewsContent(it))
        }
    )
}

@ExperimentalMaterialApi
@Composable
fun FavoriteContent(
    listArticleSaved: List<Article>,
    onClickArticle: (Article) -> Unit,
    onDelete: (Article) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), elevation = 10.dp
        ) {
            Text(
                text = "Favorite",
                fontSize = 36.sp,
                fontStyle = FontStyle.Italic,
                color = Primary,
                modifier = Modifier.padding(start = 20.dp),
                fontWeight = FontWeight.Bold
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            items(listArticleSaved.size) { index ->
                NewsItem(
                    article = listArticleSaved[index],
                    icon = R.drawable.delete,
                    onClickArticle = onClickArticle,
                    onClickIcon = onDelete
                )
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 2.dp
                )
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}
