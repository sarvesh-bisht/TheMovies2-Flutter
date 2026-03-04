package com.skydoves.themovies2.composeapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MoviePoster(
  posterUrl: String?,
  contentDescription: String,
  modifier: Modifier = Modifier
)
