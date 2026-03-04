package com.skydoves.themovies2.shared.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbMovieDto(
  val id: Int,
  val title: String,
  val overview: String,
  @SerialName("poster_path") val posterPath: String? = null
)
