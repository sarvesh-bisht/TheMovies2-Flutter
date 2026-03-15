package com.skydoves.themovies2.shared.core.network.mapper

import com.skydoves.themovies2.shared.core.domain.model.MovieSummary
import com.skydoves.themovies2.shared.core.network.model.TmdbMovieDto

internal fun TmdbMovieDto.toDomain(): MovieSummary = MovieSummary(
  id = id,
  title = title,
  overview = overview,
  posterPath = posterPath
)
