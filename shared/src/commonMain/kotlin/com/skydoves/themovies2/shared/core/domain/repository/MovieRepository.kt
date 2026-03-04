package com.skydoves.themovies2.shared.core.domain.repository

import com.skydoves.themovies2.shared.core.domain.model.MovieSummary

interface MovieRepository {
  suspend fun getPopularMovies(page: Int = 1): List<MovieSummary>
}
