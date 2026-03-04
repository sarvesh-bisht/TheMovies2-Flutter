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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skydoves.themovies2.composeapp.config.tmdbApiKeyOrNull
import com.skydoves.themovies2.shared.bootstrap.SharedContainer
import com.skydoves.themovies2.shared.feature.movielist.MovieListUiState

@Composable
fun App() {
  val stateHolder = remember { SharedContainer.movieListStateHolder(apiKey = tmdbApiKeyOrNull()) }
  val uiState by stateHolder.uiState.collectAsState()

  LaunchedEffect(Unit) {
    stateHolder.load(page = 1)
  }

  MaterialTheme {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
      Text(text = "ComposeApp movie list (Phase 3 state-holder flow)")

      if (tmdbApiKeyOrNull().isNullOrBlank()) {
        Text(text = "Using bootstrap data (no TMDB API key configured).", modifier = Modifier.padding(top = 8.dp))
      }

      when (val state = uiState) {
        is MovieListUiState.Loading -> {
          Text(text = "Loading movies...")
        }

        is MovieListUiState.Error -> {
          Text(text = "Error: ${state.message}")
        }

        is MovieListUiState.Success -> {
          LazyColumn {
            items(state.movies) { movie ->
              Text(text = "• ${movie.title}", modifier = Modifier.padding(top = 8.dp))
            }
          }
        }
      }
    }
  }
}
