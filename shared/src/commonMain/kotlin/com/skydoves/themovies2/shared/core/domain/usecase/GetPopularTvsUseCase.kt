package com.skydoves.themovies2.shared.core.domain.usecase

import com.skydoves.themovies2.shared.core.domain.model.TvSummary
import com.skydoves.themovies2.shared.core.domain.repository.MovieRepository

class GetPopularTvsUseCase(
  private val movieRepository: MovieRepository
) {
  suspend operator fun invoke(page: Int = 1): List<TvSummary> {
    return movieRepository.getPopularTvs(page)
  }
}
