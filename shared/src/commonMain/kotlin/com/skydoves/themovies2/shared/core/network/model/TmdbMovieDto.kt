package com.skydoves.themovies2.shared.core.network.model


data class TmdbMovieDto(
  val id: Int,
  val title: String,
  val overview: String,
  val posterPath: String? = null
)
