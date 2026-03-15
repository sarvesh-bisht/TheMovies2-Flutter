package com.skydoves.themovies2.shared.core.domain.usecase

import com.skydoves.themovies2.shared.core.domain.model.MovieSummary
import com.skydoves.themovies2.shared.core.domain.repository.MovieRepository

class GetPopularMoviesUseCase(
  private val movieRepository: MovieRepository
) {
  suspend operator fun invoke(page: Int = 1): List<MovieSummary> {
    return movieRepository.getPopularMovies(page)
  }
}
