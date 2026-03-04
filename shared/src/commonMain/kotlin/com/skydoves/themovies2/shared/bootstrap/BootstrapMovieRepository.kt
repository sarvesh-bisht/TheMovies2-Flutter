package com.skydoves.themovies2.shared.bootstrap

import com.skydoves.themovies2.shared.core.domain.model.MovieSummary
import com.skydoves.themovies2.shared.core.domain.repository.MovieRepository

/**
 * Temporary bootstrap repository for Phase 2.
 * Replace this with a real Ktor-backed implementation in the next step.
 */
class BootstrapMovieRepository : MovieRepository {
  override suspend fun getPopularMovies(page: Int): List<MovieSummary> {
    return listOf(
      MovieSummary(
        id = 603,
        title = "The Matrix",
        overview = "A computer hacker learns about the true nature of reality.",
        posterPath = null
      ),
      MovieSummary(
        id = 680,
        title = "Pulp Fiction",
        overview = "Stories of crime intertwine in Los Angeles.",
        posterPath = null
      )
    )
  }
}
