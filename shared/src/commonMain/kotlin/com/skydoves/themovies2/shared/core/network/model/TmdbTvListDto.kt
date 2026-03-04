package com.skydoves.themovies2.shared.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbTvListDto(
  @SerialName("results") val tvs: List<TmdbTvDto> = emptyList()
)
