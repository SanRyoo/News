package com.sanryoo.news.feature.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanryoo.news.feature.domain.modal.Article
import com.sanryoo.news.feature.domain.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _listArticleSaved = MutableStateFlow(emptyList<Article>())
    val listArticleSaved = _listArticleSaved.asStateFlow()

    private var recentlyItemDeleted: Article? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class NavigateNewsContent(val article: Article) : UiEvent()
        data class ShowSnackBar(val message: String, val label: String? = null) : UiEvent()
    }

    fun onUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch { _eventFlow.emit(uiEvent) }
    }

    init {
        databaseRepository.getAllArticles()
            .onEach {
                _listArticleSaved.value = it
            }
            .launchIn(viewModelScope)
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            databaseRepository.delete(article)
            recentlyItemDeleted = article
            _eventFlow.emit(UiEvent.ShowSnackBar("Removed the article from favorite", "UNDO"))
        }
    }

    fun undo() {
        viewModelScope.launch {
            if (recentlyItemDeleted != null) {
                databaseRepository.upsert(recentlyItemDeleted!!)
            }
        }
    }

}