package com.skydoves.themovies2.shared.core.domain.model

data class TvSummary(
  val id: Int,
  val name: String,
  val overview: String,
  val posterPath: String?
)
