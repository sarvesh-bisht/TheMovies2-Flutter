package com.skydoves.themovies2.composeapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
private val TabUnselected = Color(0xFFF2C6DA)

private enum class HomeTab { Movie, Tv, Star }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App() {
  val stateHolder = remember { SharedContainer.movieListStateHolder(apiKey = tmdbApiKeyOrNull()) }
  val uiState by stateHolder.uiState.collectAsState()
  val scope = rememberCoroutineScope()
  var selectedMovie by remember { mutableStateOf<MovieSummary?>(null) }
  var selectedTab by remember { mutableStateOf(HomeTab.Movie) }

  LaunchedEffect(Unit) {
    stateHolder.load(page = 1)
  }

  MaterialTheme {
    Surface(color = AppBackground, modifier = Modifier.fillMaxSize()) {
      Column(modifier = Modifier.fillMaxSize()) {
        TopBar()

        Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp, vertical = 8.dp)) {
          when (selectedTab) {
            HomeTab.Movie -> {
              if (selectedMovie != null) {
                MovieDetailView(movie = selectedMovie!!, onBack = { selectedMovie = null })
              } else {
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
                        MoviePosterItem(movie = movie, onClick = { selectedMovie = movie })
                      }
                    }
                  }
                }
              }
            }

            HomeTab.Tv -> PlaceholderTabContent(title = "TV", description = "TV tab migration is next.")
            HomeTab.Star -> PlaceholderTabContent(title = "Star", description = "Star tab migration is next.")
          }
        }

        BottomBar(
          selected = selectedTab,
          onSelect = {
            selectedTab = it
            if (it != HomeTab.Movie) selectedMovie = null
          }
        )
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
private fun BottomBar(
  selected: HomeTab,
  onSelect: (HomeTab) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(62.dp)
      .background(BrandPink),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
  ) {
    TabItem(label = "Movie", selected = selected == HomeTab.Movie) { onSelect(HomeTab.Movie) }
    TabItem(label = "Tv", selected = selected == HomeTab.Tv) { onSelect(HomeTab.Tv) }
    TabItem(label = "Star", selected = selected == HomeTab.Star) { onSelect(HomeTab.Star) }
  }
}

@Composable
private fun TabItem(label: String, selected: Boolean, onClick: () -> Unit) {
  Text(
    text = label,
    color = if (selected) Color.White else TabUnselected,
    fontWeight = FontWeight.Bold,
    modifier = Modifier.clickable(onClick = onClick)
  )
}

@Composable
private fun PlaceholderTabContent(title: String, description: String) {
  Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
    Text(text = title, color = TitleColor, style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = description, color = TitleColor)
  }
}

@Composable
private fun MoviePosterItem(movie: MovieSummary, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(290.dp)
      .clickable(onClick = onClick)
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

@Composable
private fun MovieDetailView(movie: MovieSummary, onBack: () -> Unit) {
  Column(modifier = Modifier.fillMaxSize()) {
    Button(onClick = onBack) {
      Text("Back")
    }
    Spacer(modifier = Modifier.height(8.dp))
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
    ) {
      MoviePoster(
        posterUrl = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
        contentDescription = movie.title,
        modifier = Modifier.fillMaxSize()
      )
    }
    Spacer(modifier = Modifier.height(12.dp))
    Text(text = movie.title, color = TitleColor, style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = movie.overview, color = TitleColor)
  }
}
