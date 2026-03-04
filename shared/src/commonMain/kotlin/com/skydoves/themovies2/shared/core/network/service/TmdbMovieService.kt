package com.skydoves.themovies2.shared.core.network.service

import com.skydoves.themovies2.shared.core.network.model.TmdbMovieListDto

interface TmdbMovieService {
  suspend fun fetchPopularMovies(page: Int = 1): TmdbMovieListDto
}
