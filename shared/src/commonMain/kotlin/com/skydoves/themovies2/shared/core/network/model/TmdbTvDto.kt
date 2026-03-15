package com.skydoves.themovies2.shared.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbTvDto(
  val id: Int,
  @SerialName("name") val name: String,
  @SerialName("overview") val overview: String,
  @SerialName("poster_path") val posterPath: String? = null
)
