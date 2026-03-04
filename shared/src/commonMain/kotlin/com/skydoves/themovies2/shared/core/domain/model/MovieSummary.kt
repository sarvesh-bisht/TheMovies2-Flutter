package com.skydoves.themovies2.shared.core.domain.model

data class MovieSummary(
  val id: Int,
  val title: String,
  val overview: String,
  val posterPath: String?
)
