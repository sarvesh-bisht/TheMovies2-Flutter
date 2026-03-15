package com.skydoves.themovies2.shared.core.domain.repository

import com.skydoves.themovies2.shared.core.domain.model.MovieSummary
import com.skydoves.themovies2.shared.core.domain.model.PersonSummary
import com.skydoves.themovies2.shared.core.domain.model.TvSummary

interface MovieRepository {
  suspend fun getPopularMovies(page: Int = 1): List<MovieSummary>
  suspend fun getPopularTvs(page: Int = 1): List<TvSummary>
  suspend fun getPopularPeople(page: Int = 1): List<PersonSummary>
}
