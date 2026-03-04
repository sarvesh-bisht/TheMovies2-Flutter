package com.skydoves.themovies2.composeapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skydoves.themovies2.shared.bootstrap.BootstrapMovieRepository
import com.skydoves.themovies2.shared.core.domain.model.MovieSummary
import com.skydoves.themovies2.shared.core.domain.usecase.GetPopularMoviesUseCase

@Composable
fun App() {
  var movies by remember { mutableStateOf<List<MovieSummary>>(emptyList()) }

  LaunchedEffect(Unit) {
    movies = GetPopularMoviesUseCase(BootstrapMovieRepository()).invoke()
  }

  MaterialTheme {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
      Text(text = "ComposeApp bootstrap (Phase 2 contracts)")
      LazyColumn {
        items(movies) { movie ->
          Text(text = "• ${movie.title}", modifier = Modifier.padding(top = 8.dp))
        }
      }
    }
  }
}
