package com.skydoves.themovies2.shared.feature.movielist

import com.skydoves.themovies2.shared.core.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieListStateHolder(
  private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) {

  private val _uiState = MutableStateFlow<MovieListUiState>(MovieListUiState.Loading)
  val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

  suspend fun load(page: Int = 1) {
    _uiState.value = MovieListUiState.Loading
    _uiState.value = runCatching { getPopularMoviesUseCase(page) }
      .fold(
        onSuccess = { MovieListUiState.Success(it) },
        onFailure = { MovieListUiState.Error(it.message ?: "Unknown error") }
      )
  }
}
