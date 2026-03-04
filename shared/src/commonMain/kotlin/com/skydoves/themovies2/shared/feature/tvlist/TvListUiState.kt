package com.skydoves.themovies2.shared.feature.tvlist

import com.skydoves.themovies2.shared.core.domain.model.TvSummary

sealed interface TvListUiState {
  data object Loading : TvListUiState
  data class Success(val tvs: List<TvSummary>) : TvListUiState
  data class Error(val message: String) : TvListUiState
}
