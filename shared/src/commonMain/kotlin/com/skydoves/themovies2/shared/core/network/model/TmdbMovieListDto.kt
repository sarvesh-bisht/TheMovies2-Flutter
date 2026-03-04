package com.skydoves.themovies2.shared.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbMovieListDto(
  val page: Int,
  @SerialName("results") val movies: List<TmdbMovieDto>
)
