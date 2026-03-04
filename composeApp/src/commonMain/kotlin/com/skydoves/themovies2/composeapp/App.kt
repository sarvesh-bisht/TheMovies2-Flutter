package com.skydoves.themovies2.composeapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.skydoves.themovies2.composeapp.config.tmdbApiKeyOrNull
import com.skydoves.themovies2.composeapp.ui.MoviePoster
import com.skydoves.themovies2.shared.bootstrap.SharedContainer
import com.skydoves.themovies2.shared.core.domain.model.MovieSummary
import com.skydoves.themovies2.shared.feature.movielist.MovieListUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App() {
  val apiKey = tmdbApiKeyOrNull()
  val stateHolder = remember { SharedContainer.movieListStateHolder(apiKey = apiKey) }
  val uiState by stateHolder.uiState.collectAsState()
  val scope = rememberCoroutineScope()

  LaunchedEffect(Unit) {
    stateHolder.load(page = 1)
  }

  MaterialTheme {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
      Text(text = "Movies", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
      Text(
        text = if (apiKey.isNullOrBlank()) "Data source: Bootstrap fallback" else "Data source: TMDB API",
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(top = 4.dp)
      )

      Spacer(modifier = Modifier.height(12.dp))

      when (val state = uiState) {
        is MovieListUiState.Loading -> Text(text = "Loading movies...")

        is MovieListUiState.Error -> {
          Text(text = "Failed to load movies", fontWeight = FontWeight.SemiBold)
          Text(text = state.message, modifier = Modifier.padding(top = 4.dp))
          Spacer(modifier = Modifier.height(8.dp))
          Button(onClick = { scope.launch { stateHolder.load(page = 1) } }) { Text("Retry") }
        }

        is MovieListUiState.Success -> {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "${state.movies.size} movies", style = MaterialTheme.typography.subtitle2)
            Button(onClick = { scope.launch { stateHolder.load(page = 1) } }) { Text("Refresh") }
          }

          Spacer(modifier = Modifier.height(8.dp))

          LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(state.movies, key = { it.id }) { movie ->
              MoviePosterItem(movie)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun MoviePosterItem(movie: MovieSummary) {
  Card(elevation = 4.dp) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Box(modifier = Modifier.fillMaxWidth().aspectRatio(0.68f)) {
        MoviePoster(
          posterUrl = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
          contentDescription = movie.title,
          modifier = Modifier.fillMaxSize()
        )
      }
      Text(
        text = movie.title,
        style = MaterialTheme.typography.subtitle2,
        fontWeight = FontWeight.Bold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(8.dp)
      )
    }
  }
}
