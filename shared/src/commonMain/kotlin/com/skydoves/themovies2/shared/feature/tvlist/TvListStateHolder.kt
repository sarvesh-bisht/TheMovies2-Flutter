package com.skydoves.themovies2.shared.feature.tvlist

import com.skydoves.themovies2.shared.core.domain.usecase.GetPopularTvsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TvListStateHolder(
  private val getPopularTvsUseCase: GetPopularTvsUseCase
) {

  private val _uiState = MutableStateFlow<TvListUiState>(TvListUiState.Loading)
  val uiState: StateFlow<TvListUiState> = _uiState.asStateFlow()

  suspend fun load(page: Int = 1) {
    _uiState.value = TvListUiState.Loading
    _uiState.value = runCatching { getPopularTvsUseCase(page) }
      .fold(
        onSuccess = { TvListUiState.Success(it) },
        onFailure = { TvListUiState.Error(it.message ?: "Unknown error") }
      )
  }
}
