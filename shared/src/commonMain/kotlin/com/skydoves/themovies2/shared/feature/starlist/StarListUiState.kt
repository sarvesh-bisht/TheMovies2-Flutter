package com.skydoves.themovies2.shared.feature.starlist

import com.skydoves.themovies2.shared.core.domain.model.PersonSummary

sealed interface StarListUiState {
  data object Loading : StarListUiState
  data class Success(val people: List<PersonSummary>) : StarListUiState
  data class Error(val message: String) : StarListUiState
}
