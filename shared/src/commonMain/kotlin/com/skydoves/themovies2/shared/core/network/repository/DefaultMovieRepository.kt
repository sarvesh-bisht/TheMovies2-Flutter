package com.skydoves.themovies2.shared.core.network.repository

import com.skydoves.themovies2.shared.core.domain.model.MovieSummary
import com.skydoves.themovies2.shared.core.domain.model.PersonSummary
import com.skydoves.themovies2.shared.core.domain.model.TvSummary
import com.skydoves.themovies2.shared.core.domain.repository.MovieRepository
import com.skydoves.themovies2.shared.core.network.mapper.toDomain
import com.skydoves.themovies2.shared.core.network.service.TmdbMovieService

class DefaultMovieRepository(
  private val movieService: TmdbMovieService
) : MovieRepository {

  override suspend fun getPopularMovies(page: Int): List<MovieSummary> {
    return movieService.fetchPopularMovies(page).movies.map { it.toDomain() }
  }

  override suspend fun getPopularTvs(page: Int): List<TvSummary> {
    return movieService.fetchPopularTvs(page).tvs.map { it.toDomain() }
  }

  override suspend fun getPopularPeople(page: Int): List<PersonSummary> {
    return movieService.fetchPopularPeople(page).people.map { it.toDomain() }
  }
}
