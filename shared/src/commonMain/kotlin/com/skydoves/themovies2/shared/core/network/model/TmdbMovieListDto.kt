package com.skydoves.themovies2.shared.core.network.model


data class TmdbMovieListDto(
  val page: Int,
  val movies: List<TmdbMovieDto>
)
