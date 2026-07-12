package com.amro.feature.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.amro.application.movie.model.MovieSortField
import com.amro.application.movie.model.SortDirection
import com.amro.application.movie.model.TrendingQuery
import com.amro.application.movie.policy.TrendingMoviesPolicy
import com.amro.application.movie.repository.MovieRepository
import com.amro.application.movie.usecase.GetTrendingMoviesUseCase
import com.amro.application.movie.usecase.ObserveGenresUseCase
import com.amro.application.movie.usecase.RefreshTrendingMoviesUseCase
import com.amro.domain.movie.model.Genre
import com.amro.domain.movie.model.MovieProviderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TrendingUiState(
    val query: TrendingQuery = TrendingQuery(),
    val genres: List<Genre> = emptyList(),
    val refreshing: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TrendingViewModel @Inject constructor(
    repository: MovieRepository,
    policy: TrendingMoviesPolicy,
    // TODO: @Koen Inject use-cases instead?
) : ViewModel() {
    private val getMovies = GetTrendingMoviesUseCase(repository);
    private val refreshUseCase = RefreshTrendingMoviesUseCase(repository, policy)
    private val observeGenresUseCase = ObserveGenresUseCase(repository)
    private val _state = MutableStateFlow(TrendingUiState())
    val state = _state.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val movies = _state.map { it.query }
        .distinctUntilChanged()
        .flatMapLatest(getMovies::invoke)
        .cachedIn(viewModelScope)

    init {
        observeGenres()
        refresh(force = false)
    }

    private fun observeGenres() = viewModelScope.launch {
        observeGenresUseCase(
            provider = MovieProviderType.TMDB,
            language = "en-US",
        ).collect { genres ->
            _state.update {
                it.copy(genres = genres)
            }
        }
    }

    fun search(value: String) = updateQuery {
        copy(search = value)
    }

    fun toggleGenre(id: String) = updateQuery {
        copy(
            genreIds = if (id in genreIds) {
                genreIds - id
            } else {
                genreIds + id
            }
        )
    }

    fun sort(field: MovieSortField) = updateQuery {
        // Same field, simply switch sort direction:
        if (sortField == field) copy(
            direction = if (direction == SortDirection.ASCENDING) SortDirection.DESCENDING else SortDirection.ASCENDING
        )
        // Switch field and use default associated sorting
        else copy(
            sortField = field,
            direction = field.defaultSortDirection
        )
    }

    fun refresh(force: Boolean = true) {
        viewModelScope.launch {
            _state.update {
                it.copy(refreshing = true, error = null)
            }

            refreshUseCase(
                _state.value.query,
                force
            ).onFailure { e ->
                _state.update {
                    it.copy(error = e.message)
                }
            }
            _state.update {
                it.copy(refreshing = false)
            }
        }
    }

    private fun updateQuery(block: TrendingQuery.() -> TrendingQuery) {
        _state.update { it.copy(query = it.query.block()) }
    }
}