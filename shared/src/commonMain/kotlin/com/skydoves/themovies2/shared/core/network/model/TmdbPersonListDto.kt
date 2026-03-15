package com.skydoves.themovies2.shared.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbPersonListDto(
  @SerialName("results") val people: List<TmdbPersonDto> = emptyList()
)
