package com.amro.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amro.application.movie.usecase.ObserveMovieDetailsUseCase
import com.amro.application.movie.usecase.RefreshMovieDetailsUseCase
import com.amro.core.common.locale.LanguageProvider
import com.amro.domain.movie.model.MovieIdentifier
import com.amro.domain.movie.model.MovieProviderType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val observeMovieDetails: ObserveMovieDetailsUseCase,
    private val refreshMovieDetails: RefreshMovieDetailsUseCase,
    private val languageProvider: LanguageProvider,
) : ViewModel() {
    private val _state = MutableStateFlow(MovieDetailsUiState())
    val state = _state.asStateFlow()
    private var id: MovieIdentifier? = null
    private var observeJob: Job? = null
    private var refreshJob: Job? = null

    fun initialize(provider: MovieProviderType, movieId: String) {
        val newId = MovieIdentifier(provider, movieId)
        if (id == newId) return
        id = newId
        observe(newId)
        refresh(showFullLoader = true)
    }

    fun onRefreshRequested() = refresh(showFullLoader = false)
    fun onRetryRequested() = refresh(showFullLoader = true)

    private fun observe(identifier: MovieIdentifier) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            languageProvider.language.map { it.tag }.distinctUntilChanged().collectLatest { language ->
                observeMovieDetails(identifier, language).collectLatest { movie ->
                    _state.update { it.copy(movie = movie, loading = movie == null && it.loading) }
                }
            }
        }
    }

    private fun refresh(showFullLoader: Boolean) {
        val identifier = id ?: return
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    loading = showFullLoader && it.movie == null,
                    refreshing = !showFullLoader,
                    error = null,
                )
            }
            val result = refreshMovieDetails(identifier, languageProvider.language.value.tag)
            _state.update {
                it.copy(
                    loading = false,
                    refreshing = false,
                    error = result.exceptionOrNull()?.let { MovieDetailsUiError.LoadingFailed },
                )
            }
        }
    }
}
