package com.skydoves.themovies2.shared.core.network.service

import com.skydoves.themovies2.shared.core.network.model.TmdbMovieListDto
import com.skydoves.themovies2.shared.core.network.model.TmdbPersonListDto
import com.skydoves.themovies2.shared.core.network.model.TmdbTvListDto

interface TmdbMovieService {
  suspend fun fetchPopularMovies(page: Int = 1): TmdbMovieListDto
  suspend fun fetchPopularTvs(page: Int = 1): TmdbTvListDto
  suspend fun fetchPopularPeople(page: Int = 1): TmdbPersonListDto
}
