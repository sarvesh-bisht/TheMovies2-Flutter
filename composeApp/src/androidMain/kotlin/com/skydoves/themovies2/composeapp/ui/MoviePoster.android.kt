package com.skydoves.themovies2.composeapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage

@Composable
actual fun MoviePoster(
  posterUrl: String?,
  contentDescription: String,
  modifier: Modifier
) {
  if (posterUrl.isNullOrBlank()) {
    Box(
      modifier = modifier.background(MaterialTheme.colors.onSurface.copy(alpha = 0.08f)),
      contentAlignment = Alignment.Center
    ) {
      Text(text = "No Image", style = MaterialTheme.typography.caption)
    }
  } else {
    AsyncImage(
      model = posterUrl,
      contentDescription = contentDescription,
      modifier = modifier
    )
  }
}
