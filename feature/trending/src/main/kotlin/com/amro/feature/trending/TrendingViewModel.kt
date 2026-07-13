package com.amro.feature.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.amro.application.movie.model.MovieSortField
import com.amro.application.movie.model.SortDirection
import com.amro.application.movie.model.TrendingQuery
import com.amro.application.movie.usecase.GetTrendingMoviesUseCase
import com.amro.application.movie.usecase.ObserveGenresUseCase
import com.amro.application.movie.usecase.RefreshTrendingMoviesUseCase
import com.amro.core.common.locale.LanguageProvider
import com.amro.domain.movie.model.MovieProviderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val getTrendingMovies: GetTrendingMoviesUseCase,
    private val refreshTrendingMovies: RefreshTrendingMoviesUseCase,
    private val observeGenres: ObserveGenresUseCase,
    private val languageProvider: LanguageProvider,
) : ViewModel() {

    private val _state = MutableStateFlow(
        TrendingUiState(
            query = TrendingQuery(
                provider = MovieProviderType.TMDB,
                language = languageProvider.language.value.tag,
            ),
        ),
    )
    val state = _state.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val movies = _state
        .map { it.query }
        .distinctUntilChanged()
        .flatMapLatest(getTrendingMovies::invoke)
        .cachedIn(viewModelScope)

    private var refreshJob: Job? = null

    init {
        observeGenres()
        observeLanguageChanges()
        refresh(force = false)
    }

    fun onSearchChanged(value: String) = updateQuery { copy(search = value) }

    fun onGenreToggled(id: String) = updateQuery {
        copy(genreIds = if (id in genreIds) genreIds - id else genreIds + id)
    }

    fun onSortSelected(field: MovieSortField) = updateQuery {
        if (sortField == field) {
            copy(direction = direction.toggle())
        } else {
            copy(sortField = field, direction = field.defaultSortDirection)
        }
    }

    fun onRefreshRequested() = refresh(force = true)
    fun onRetryRequested() = refresh(force = true)

    private fun refresh(force: Boolean) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    refreshing = true,
                    error = null
                )
            }
            val result = refreshTrendingMovies(_state.value.query, force)
            _state.update {
                it.copy(
                    refreshing = false,
                    error = result.exceptionOrNull()?.let { TrendingUiError.RefreshFailed },
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeGenres() {
        viewModelScope.launch {
            _state.map {
                GenreObservationKey(it.query.provider, it.query.language)
            }.distinctUntilChanged()
                .flatMapLatest { observeGenres(it.provider, it.language) }
                .collectLatest { genres -> _state.update { it.copy(genres = genres) } }
        }
    }

    private fun observeLanguageChanges() {
        viewModelScope.launch {
            languageProvider.language
                .map { it.tag }
                .distinctUntilChanged()
                .drop(1)
                .collectLatest { language ->
                    updateQuery { copy(language = language, genreIds = emptySet()) }
                    refresh(force = false)
                }
        }
    }

    private fun updateQuery(transform: TrendingQuery.() -> TrendingQuery) {
        _state.update { it.copy(query = it.query.transform()) }
    }

    private fun SortDirection.toggle() = when (this) {
        SortDirection.ASCENDING -> SortDirection.DESCENDING
        SortDirection.DESCENDING -> SortDirection.ASCENDING
    }

    private data class GenreObservationKey(
        val provider: MovieProviderType,
        val language: String,
    )
}
