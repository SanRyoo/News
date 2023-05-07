package com.sanryoo.news.feature.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanryoo.news.feature.domain.modal.Article
import com.sanryoo.news.feature.domain.repository.APIRepository
import com.sanryoo.news.feature.domain.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val apiRepository: APIRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _listSearched = MutableStateFlow(emptyList<Article>())
    val listSearched = _listSearched.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searching = MutableStateFlow(false)
    val searching = _searching.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val pageNumber = 1

    sealed class UiEvent {
        data class NavigateToNewsContent(val article: Article) : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
    }

    init {
        searchText
            .debounce(700L)
            .onEach {
                if (it.isBlank()) {
                    _listSearched.value = emptyList()
                } else {
                    searchNews()
                }
            }
            .launchIn(viewModelScope)
    }

    fun onUiEvent(event: UiEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun onSearchTextChange(newSearchText: String) {
        _searchText.value = newSearchText
    }

    private fun searchNews() {
        viewModelScope.launch {
            try {
                _searching.value = true
                val response = apiRepository.searchForNews(searchText.value, pageNumber)
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item?.status == "ok") {
                        _listSearched.value = item.articles
                        _searching.value = false
                    } else {
                        _searching.value = false
                    }
                } else {
                    _searching.value = false
                }
            } catch (e: Exception) {
                return@launch
            }
        }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            val id = databaseRepository.upsert(article)
            if (id > 0) {
                _eventFlow.emit(UiEvent.ShowSnackBar(message = "Added the article to favorite"))
            }
        }
    }

}