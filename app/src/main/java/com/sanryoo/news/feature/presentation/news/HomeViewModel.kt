package com.sanryoo.news.feature.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanryoo.news.feature.domain.modal.Article
import com.sanryoo.news.feature.domain.repository.APIRepository
import com.sanryoo.news.feature.domain.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiRepository: APIRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _listArticle = MutableStateFlow(emptyList<Article>())
    val listArticle = _listArticle.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _loadingMore = MutableStateFlow(false)
    val loadingMore = _loadingMore.asStateFlow()

    private val _caughtAllNews = MutableStateFlow(false)
    val caughtAllNews = _caughtAllNews.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val countryCode = "us"
    private var breakingNewsPage = 1

    sealed class UiEvent {
        data class NavigateNewsContent(val article: Article) : UiEvent()
        data class ShowSnackBar(val message: String, val label: String? = null) : UiEvent()
    }

    fun onUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch { _eventFlow.emit(uiEvent) }
    }

    init {
        getBreakingNews()
    }

    fun getBreakingNews() {
        viewModelScope.launch {
            try {
                _loading.value = true
                breakingNewsPage = 1
                val response = apiRepository.getBreakingNews(countryCode, breakingNewsPage)
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item?.status == "ok") {
                        _caughtAllNews.value = false
                        _listArticle.value = item.articles
                        _loading.value = false
                    } else {
                        _loading.value = false
                        _eventFlow.emit(UiEvent.ShowSnackBar(message = "Can not load articles"))
                    }
                } else {
                    _loading.value = false
                    _eventFlow.emit(UiEvent.ShowSnackBar(message = "Can not load articles"))
                }
            } catch (e: IOException) {
                _loading.value = false
                _eventFlow.emit(UiEvent.ShowSnackBar(message = "Can not load due to connection"))
                return@launch
            } catch (e: HttpException) {
                _loading.value = false
                _eventFlow.emit(UiEvent.ShowSnackBar(message = "Can not load due to connection"))
                return@launch
            } catch (e: SocketTimeoutException) {
                _loading.value = false
                _eventFlow.emit(UiEvent.ShowSnackBar(message = "Can not load due to connection"))
                return@launch
            } catch (e: Exception) {
                _loading.value = false
                _eventFlow.emit(UiEvent.ShowSnackBar(message = "Can not load articles"))
                return@launch
            }
        }
    }

    fun loadMoreBreakingNews() {
        viewModelScope.launch {
            try {
                _loadingMore.value = true
                breakingNewsPage++
                val response = apiRepository.getBreakingNews(countryCode, breakingNewsPage)
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item?.status == "ok") {
                        if (item.articles.isNotEmpty()) {
                            _listArticle.value += item.articles
                            _loadingMore.value = false
                        } else {
                            _caughtAllNews.value = true
                            _loadingMore.value = false
                            _eventFlow.emit(UiEvent.ShowSnackBar(message = "You have caught all the news"))
                            return@launch
                        }
                    } else {
                        _caughtAllNews.value = true
                        _loadingMore.value = false
                        _eventFlow.emit(UiEvent.ShowSnackBar(message = "Can not load articles"))
                    }
                } else {
                    _caughtAllNews.value = true
                    _loadingMore.value = false
                    _eventFlow.emit(UiEvent.ShowSnackBar(message = "Can not load articles"))
                }
            } catch (e: Exception) {
                _caughtAllNews.value = true
                _loadingMore.value = false
                _eventFlow.emit(UiEvent.ShowSnackBar(message = "Can not load articles"))
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