package com.skydoves.themovies2.composeapp.config

import com.skydoves.themovies2.composeapp.BuildConfig

actual fun tmdbApiKeyOrNull(): String? {
  val key = BuildConfig.TMDB_API_KEY
  return key.takeIf { it.isNotBlank() }
}
