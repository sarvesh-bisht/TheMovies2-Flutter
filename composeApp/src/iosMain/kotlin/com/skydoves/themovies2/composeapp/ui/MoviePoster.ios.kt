package com.skydoves.themovies2.composeapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
actual fun MoviePoster(
  posterUrl: String?,
  contentDescription: String,
  modifier: Modifier
) {
  Box(
    modifier = modifier.background(MaterialTheme.colors.onSurface.copy(alpha = 0.08f)),
    contentAlignment = Alignment.Center
  ) {
    Text(text = if (posterUrl.isNullOrBlank()) "No Image" else "Poster", style = MaterialTheme.typography.caption)
  }
}
