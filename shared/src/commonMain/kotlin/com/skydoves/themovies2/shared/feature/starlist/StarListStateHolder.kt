package com.skydoves.themovies2.shared.feature.starlist

import com.skydoves.themovies2.shared.core.domain.usecase.GetPopularPeopleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StarListStateHolder(
  private val getPopularPeopleUseCase: GetPopularPeopleUseCase
) {

  private val _uiState = MutableStateFlow<StarListUiState>(StarListUiState.Loading)
  val uiState: StateFlow<StarListUiState> = _uiState.asStateFlow()

  suspend fun load(page: Int = 1) {
    _uiState.value = StarListUiState.Loading
    _uiState.value = runCatching { getPopularPeopleUseCase(page) }
      .fold(
        onSuccess = { StarListUiState.Success(it) },
        onFailure = { StarListUiState.Error(it.message ?: "Unknown error") }
      )
  }
}
