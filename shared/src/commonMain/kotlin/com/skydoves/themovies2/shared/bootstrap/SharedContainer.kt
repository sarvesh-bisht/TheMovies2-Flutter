package com.skydoves.themovies2.shared.bootstrap

import com.skydoves.themovies2.shared.core.domain.repository.MovieRepository
import com.skydoves.themovies2.shared.core.network.client.HttpClientFactory
import com.skydoves.themovies2.shared.core.network.repository.DefaultMovieRepository
import com.skydoves.themovies2.shared.core.network.service.KtorTmdbMovieService
import com.skydoves.themovies2.shared.core.domain.usecase.GetPopularMoviesUseCase
import com.skydoves.themovies2.shared.feature.movielist.MovieListStateHolder

object SharedContainer {

  fun movieListStateHolder(apiKey: String?): MovieListStateHolder {
    return MovieListStateHolder(
      getPopularMoviesUseCase = GetPopularMoviesUseCase(movieRepository(apiKey))
    )
  }

  /**
   * Use `apiKey` when you want real network data.
   * If null/blank, we fallback to bootstrap in-memory data.
   */
  fun movieRepository(apiKey: String?): MovieRepository {
    if (apiKey.isNullOrBlank()) return BootstrapMovieRepository()

    val service = KtorTmdbMovieService(
      client = HttpClientFactory.create(),
      apiKey = apiKey
    )
    return DefaultMovieRepository(service)
  }
}
