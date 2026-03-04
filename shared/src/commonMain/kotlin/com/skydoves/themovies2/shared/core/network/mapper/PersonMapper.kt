package com.skydoves.themovies2.shared.core.network.mapper

import com.skydoves.themovies2.shared.core.domain.model.PersonSummary
import com.skydoves.themovies2.shared.core.network.model.TmdbPersonDto

fun TmdbPersonDto.toDomain(): PersonSummary = PersonSummary(
  id = id,
  name = name,
  knownForDepartment = knownForDepartment,
  profilePath = profilePath
)
