package com.sanryoo.news.feature.presentation.search

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.sanryoo.news.R
import com.sanryoo.news.feature.domain.modal.Article
import com.sanryoo.news.feature.presentation._component.NewsItem
import com.sanryoo.news.feature.util.Screen
import com.sanryoo.news.ui.theme.Primary
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

@FlowPreview
@ExperimentalMaterialApi
@Composable
fun SearchScreen(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: SearchViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is SearchViewModel.UiEvent.NavigateToNewsContent -> {
                    val articleJson = Gson().toJson(event.article)
                    navController.navigate("${Screen.NewsContent.route}?article=${articleJson}&saved=false")
                }
                is SearchViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }
    val searchText = viewModel.searchText.collectAsStateWithLifecycle().value
    val searching = viewModel.searching.collectAsStateWithLifecycle().value
    val listSearched = viewModel.listSearched.collectAsStateWithLifecycle().value
    SearchContent(
        searchText,
        viewModel::onSearchTextChange,
        searching,
        viewModel::saveArticle,
        onClickArticle = {
            viewModel.onUiEvent(SearchViewModel.UiEvent.NavigateToNewsContent(it))
        },
        listSearched
    )
}

@ExperimentalMaterialApi
@Composable
fun SearchContent(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    searching: Boolean,
    onFavorite: (Article) -> Unit,
    onClickArticle: (Article) -> Unit,
    listSearched: List<Article>,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember {
        FocusRequester()
    }
    val scrollState = rememberLazyListState()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.isScrollInProgress }
            .filter { it }
            .collect {
                focusManager.clearFocus()
            }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 5.dp, horizontal = 10.dp)
                    .focusRequester(focusRequester),
                shape = RoundedCornerShape(20.dp),
                maxLines = 1
            )
            Icon(
                painter = painterResource(id = R.drawable.search_selected),
                contentDescription = "Search icon",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(30.dp),
                tint = Primary
            )
        }
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {
                    focusManager.clearFocus()
                }
            ),
            state = scrollState
        ) {
            if (searching) {
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .size(35.dp)
                                .align(Alignment.Center),
                            strokeWidth = 4.dp,
                        )
                    }
                }
            }
            items(listSearched.size) { index ->
                NewsItem(
                    article = listSearched[index],
                    icon = R.drawable.favorite_unselected,
                    onClickIcon = onFavorite,
                    onClickArticle = onClickArticle
                )
                Divider(
                    modifier = Modifier.fillMaxWidth(), thickness = 2.dp
                )
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}
