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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val getTrendingMovies: GetTrendingMoviesUseCase,
    private val refreshTrendingMovies: RefreshTrendingMoviesUseCase,
    private val observeGenres: ObserveGenresUseCase,
    private val languageProvider: LanguageProvider,
) : ViewModel() {

    private val initialLanguage =
        languageProvider.language.value.tag

    private val _state = MutableStateFlow(
        TrendingUiState(
            query = TrendingQuery(
                language = initialLanguage,
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

    init {
        observeGenres()
        observeLanguageChanges()
        refresh(force = false)
    }

    fun search(value: String) {
        updateQuery {
            copy(search = value)
        }
    }

    fun toggleGenre(id: String) {
        updateQuery {
            copy(
                genreIds = if (id in genreIds) {
                    genreIds - id
                } else {
                    genreIds + id
                },
            )
        }
    }

    fun sort(field: MovieSortField) {
        updateQuery {
            if (sortField == field) {
                copy(
                    direction = direction.toggle(),
                )
            } else {
                copy(
                    sortField = field,
                    direction = field.defaultSortDirection,
                )
            }
        }
    }

    fun refresh(force: Boolean = true) {
        viewModelScope.launch {
            val query = _state.value.query

            _state.update {
                it.copy(
                    refreshing = true,
                    error = null,
                )
            }

            refreshTrendingMovies(
                query = query,
                force = force,
            ).onFailure { exception ->
                _state.update {
                    it.copy(
                        error = exception.message ?: "Unable to refresh trending movies.",
                    )
                }
            }

            _state.update {
                it.copy(refreshing = false)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeGenres() {
        viewModelScope.launch {
            _state
                .map { state ->
                    GenreQuery(
                        provider = state.query.provider,
                        language = state.query.language,
                    )
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    observeGenres(
                        provider = query.provider,
                        language = query.language,
                    )
                }
                .collectLatest { genres ->
                    _state.update {
                        it.copy(genres = genres)
                    }
                }
        }
    }

    private fun observeLanguageChanges() {
        viewModelScope.launch {
            languageProvider.language
                .map { language -> language.tag }
                .distinctUntilChanged()
                .drop(1)
                .collectLatest { language ->
                    updateQuery {
                        copy(language = language)
                    }

                    refresh(force = false)
                }
        }
    }

    private fun updateQuery(
        transform: TrendingQuery.() -> TrendingQuery,
    ) {
        _state.update { state ->
            state.copy(
                query = state.query.transform(),
            )
        }
    }

    private fun SortDirection.toggle(): SortDirection =
        when (this) {
            SortDirection.ASCENDING -> SortDirection.DESCENDING
            SortDirection.DESCENDING -> SortDirection.ASCENDING
        }

    private data class GenreQuery(
        val provider: com.amro.domain.movie.model.MovieProviderType,
        val language: String,
    )
}