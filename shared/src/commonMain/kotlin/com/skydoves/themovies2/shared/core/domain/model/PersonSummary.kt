package com.skydoves.themovies2.shared.core.domain.model

data class PersonSummary(
  val id: Int,
  val name: String,
  val knownForDepartment: String?,
  val profilePath: String?
)
