package com.skydoves.themovies2.shared.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbPersonDto(
  val id: Int,
  @SerialName("name") val name: String,
  @SerialName("known_for_department") val knownForDepartment: String? = null,
  @SerialName("profile_path") val profilePath: String? = null
)
