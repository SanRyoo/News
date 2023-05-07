package com.sanryoo.news.feature.presentation.news_content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanryoo.news.feature.domain.modal.Article
import com.sanryoo.news.feature.domain.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsContentViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        object NavigateBack : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
    }

    fun onUiEvent(event: UiEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            val id = databaseRepository.upsert(article)
            if (id > 0) {
                _eventFlow.emit(UiEvent.ShowSnackBar("Added the article to favorite"))
            }
        }
    }

}