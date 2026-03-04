package com.skydoves.themovies2.composeapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.skydoves.themovies2.composeapp.config.tmdbApiKeyOrNull
import com.skydoves.themovies2.composeapp.ui.MoviePoster
import com.skydoves.themovies2.shared.bootstrap.SharedContainer
import com.skydoves.themovies2.shared.core.domain.model.MovieSummary
import com.skydoves.themovies2.shared.feature.movielist.MovieListUiState
import kotlinx.coroutines.launch

private val AppBackground = Color(0xFF121212)
private val BrandPink = Color(0xFFC51162)
private val OverlayBackground = Color(0xAA3D0B2C)
private val TitleColor = Color.White

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App() {
  val stateHolder = remember { SharedContainer.movieListStateHolder(apiKey = tmdbApiKeyOrNull()) }
  val uiState by stateHolder.uiState.collectAsState()
  val scope = rememberCoroutineScope()

  LaunchedEffect(Unit) {
    stateHolder.load(page = 1)
  }

  MaterialTheme {
    Surface(color = AppBackground, modifier = Modifier.fillMaxSize()) {
      Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
          TopBar()

          Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp, vertical = 8.dp)) {
            when (val state = uiState) {
              is MovieListUiState.Loading -> Text(text = "Loading movies...", color = TitleColor)

              is MovieListUiState.Error -> {
                Column {
                  Text(text = "Failed to load movies", fontWeight = FontWeight.SemiBold, color = TitleColor)
                  Text(text = state.message, modifier = Modifier.padding(top = 4.dp), color = TitleColor)
                  Spacer(modifier = Modifier.height(8.dp))
                  Button(onClick = { scope.launch { stateHolder.load(page = 1) } }) { Text("Retry") }
                }
              }

              is MovieListUiState.Success -> {
                LazyVerticalGrid(
                  columns = GridCells.Fixed(2),
                  horizontalArrangement = Arrangement.spacedBy(2.dp),
                  verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                  items(state.movies, key = { it.id }) { movie ->
                    MoviePosterItem(movie)
                  }
                }
              }
            }
          }

          BottomBar()
        }
      }
    }
  }
}

@Composable
private fun TopBar() {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(64.dp)
      .background(BrandPink)
      .padding(horizontal = 16.dp),
    contentAlignment = Alignment.CenterStart
  ) {
    Text(
      text = "TheMovies2",
      color = Color.White,
      style = MaterialTheme.typography.h5,
      fontWeight = FontWeight.Bold
    )
  }
}

@Composable
private fun BottomBar() {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(62.dp)
      .background(BrandPink),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = "Movie", color = Color.White, fontWeight = FontWeight.Bold)
    Text(text = "Tv", color = Color.White, fontWeight = FontWeight.Bold)
    Text(text = "Star", color = Color.White, fontWeight = FontWeight.Bold)
  }
}

@Composable
private fun MoviePosterItem(movie: MovieSummary) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(290.dp)
  ) {
    MoviePoster(
      posterUrl = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
      contentDescription = movie.title,
      modifier = Modifier
        .fillMaxSize()
        .background(Color.DarkGray)
    )

    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .height(50.dp)
        .background(OverlayBackground),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = movie.title,
        color = TitleColor,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(horizontal = 6.dp)
      )
    }
  }
}
