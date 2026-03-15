package com.skydoves.themovies2.shared.core.network.mapper

import com.skydoves.themovies2.shared.core.domain.model.TvSummary
import com.skydoves.themovies2.shared.core.network.model.TmdbTvDto

fun TmdbTvDto.toDomain(): TvSummary = TvSummary(
  id = id,
  name = name,
  overview = overview,
  posterPath = posterPath
)
