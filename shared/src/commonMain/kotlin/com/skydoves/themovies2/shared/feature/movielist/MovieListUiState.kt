package com.skydoves.themovies2.shared.feature.movielist

import com.skydoves.themovies2.shared.core.domain.model.MovieSummary

sealed interface MovieListUiState {
  data object Loading : MovieListUiState
  data class Success(val movies: List<MovieSummary>) : MovieListUiState
  data class Error(val message: String) : MovieListUiState
}
