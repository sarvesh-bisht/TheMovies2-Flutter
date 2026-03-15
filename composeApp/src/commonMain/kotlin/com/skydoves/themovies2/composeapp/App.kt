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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.skydoves.themovies2.composeapp.config.tmdbApiKeyOrNull
import com.skydoves.themovies2.composeapp.ui.MoviePoster
import com.skydoves.themovies2.shared.bootstrap.SharedContainer
import com.skydoves.themovies2.shared.core.domain.model.MovieSummary
import com.skydoves.themovies2.shared.core.domain.model.PersonSummary
import com.skydoves.themovies2.shared.core.domain.model.TvSummary
import com.skydoves.themovies2.shared.feature.movielist.MovieListUiState
import com.skydoves.themovies2.shared.feature.starlist.StarListUiState
import com.skydoves.themovies2.shared.feature.tvlist.TvListUiState

private val AppBackground = Color(0xFF121212)
private val BrandPink = Color(0xFFC51162)
private val OverlayBackground = Color(0xAA3D0B2C)
private val TitleColor = Color.White
private val TabUnselected = Color(0xFFF2C6DA)

private enum class HomeTab(val label: String, val icon: ImageVector, val title: String) {
  Movie("Movie", Icons.Default.LocalMovies, "TheMovies2"),
  Tv("Tv", Icons.Default.LiveTv, "TheMovies2"),
  Star("Star", Icons.Default.Star, "TheMovies2")
}

private sealed interface DetailItem {
  data class Movie(val data: MovieSummary) : DetailItem
  data class Tv(val data: TvSummary) : DetailItem
  data class Person(val data: PersonSummary) : DetailItem
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App() {
  val apiKey = tmdbApiKeyOrNull()
  val movieStateHolder = remember { SharedContainer.movieListStateHolder(apiKey = apiKey) }
  val tvStateHolder = remember { SharedContainer.tvListStateHolder(apiKey = apiKey) }
  val starStateHolder = remember { SharedContainer.starListStateHolder(apiKey = apiKey) }

  val movieUiState by movieStateHolder.uiState.collectAsState()
  val tvUiState by tvStateHolder.uiState.collectAsState()
  val starUiState by starStateHolder.uiState.collectAsState()

  var selectedDetail by remember { mutableStateOf<DetailItem?>(null) }
  var selectedTab by remember { mutableStateOf(HomeTab.Movie) }

  LaunchedEffect(Unit) {
    movieStateHolder.load(page = 1)
    tvStateHolder.load(page = 1)
    starStateHolder.load(page = 1)
  }

  val toolbarTitle = when (val detail = selectedDetail) {
    is DetailItem.Movie -> detail.data.title
    is DetailItem.Tv -> detail.data.name
    is DetailItem.Person -> detail.data.name
    null -> "TheMovies2"
  }

  MaterialTheme {
    Surface(color = AppBackground, modifier = Modifier.fillMaxSize()) {
      Column(modifier = Modifier.fillMaxSize()) {
        TopBar(title = toolbarTitle, onBack = if (selectedDetail != null) ({ selectedDetail = null }) else null)

        Box(
          modifier = Modifier
            .weight(1f)
            .then(if (selectedDetail == null) Modifier.padding(horizontal = 8.dp, vertical = 8.dp) else Modifier)
        ) {
          when (val detail = selectedDetail) {
            is DetailItem.Movie -> PosterDetailView(
              title = detail.data.title,
              overview = detail.data.overview,
              posterPath = detail.data.posterPath,
              metadata = "Movie"
            )

            is DetailItem.Tv -> PosterDetailView(
              title = detail.data.name,
              overview = detail.data.overview,
              posterPath = detail.data.posterPath,
              metadata = "TV"
            )

            is DetailItem.Person -> PosterDetailView(
              title = detail.data.name,
              overview = detail.data.knownForDepartment ?: "",
              posterPath = detail.data.profilePath,
              metadata = "Star"
            )

            null -> when (selectedTab) {
              HomeTab.Movie -> MovieGridContent(uiState = movieUiState, onMovieClick = {
                selectedDetail = DetailItem.Movie(it)
              })

              HomeTab.Tv -> TvGridContent(uiState = tvUiState, onTvClick = {
                selectedDetail = DetailItem.Tv(it)
              })

              HomeTab.Star -> StarGridContent(uiState = starUiState, onPersonClick = {
                selectedDetail = DetailItem.Person(it)
              })
            }
          }
        }

        if (selectedDetail == null) {
          BottomBar(selected = selectedTab, onSelect = { selectedTab = it })
        }
      }
    }
  }
}

@Composable
private fun TopBar(title: String, onBack: (() -> Unit)? = null) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(64.dp)
      .background(BrandPink)
      .padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (onBack != null) {
      Icon(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = "Back",
        tint = Color.White,
        modifier = Modifier.clickable(onClick = onBack)
      )
      Spacer(modifier = Modifier.width(8.dp))
    }
    Text(text = title, color = Color.White, style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
  }
}

@Composable
private fun BottomBar(selected: HomeTab, onSelect: (HomeTab) -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(64.dp)
      .background(BrandPink),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
  ) {
    TabItem(tab = HomeTab.Movie, selected = selected == HomeTab.Movie, onClick = { onSelect(HomeTab.Movie) })
    TabItem(tab = HomeTab.Tv, selected = selected == HomeTab.Tv, onClick = { onSelect(HomeTab.Tv) })
    TabItem(tab = HomeTab.Star, selected = selected == HomeTab.Star, onClick = { onSelect(HomeTab.Star) })
  }
}

@Composable
private fun TabItem(tab: HomeTab, selected: Boolean, onClick: () -> Unit) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.clickable(onClick = onClick)
  ) {
    Icon(imageVector = tab.icon, contentDescription = tab.label, tint = if (selected) Color.White else TabUnselected)
    Text(text = tab.label, color = if (selected) Color.White else TabUnselected, fontWeight = FontWeight.Bold)
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MovieGridContent(uiState: MovieListUiState, onMovieClick: (MovieSummary) -> Unit) {
  when (uiState) {
    is MovieListUiState.Loading -> LoadingText()
    is MovieListUiState.Error -> ErrorText(uiState.message)
    is MovieListUiState.Success -> {
      if (uiState.movies.isEmpty()) {
        EmptyText("No movies available.")
      } else {
        PosterGrid(uiState.movies) { movie ->
          PosterGridItem(title = movie.title, posterPath = movie.posterPath, onClick = { onMovieClick(movie) })
        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TvGridContent(uiState: TvListUiState, onTvClick: (TvSummary) -> Unit) {
  when (uiState) {
    is TvListUiState.Loading -> LoadingText()
    is TvListUiState.Error -> ErrorText(uiState.message)
    is TvListUiState.Success -> {
      if (uiState.tvs.isEmpty()) {
        EmptyText("No TV shows available.")
      } else {
        PosterGrid(uiState.tvs) { tv ->
          PosterGridItem(title = tv.name, posterPath = tv.posterPath, onClick = { onTvClick(tv) })
        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StarGridContent(uiState: StarListUiState, onPersonClick: (PersonSummary) -> Unit) {
  when (uiState) {
    is StarListUiState.Loading -> LoadingText()
    is StarListUiState.Error -> ErrorText(uiState.message)
    is StarListUiState.Success -> {
      if (uiState.people.isEmpty()) {
        EmptyText("No stars available.")
      } else {
        PosterGrid(uiState.people) { person ->
          PosterGridItem(title = person.name, posterPath = person.profilePath, onClick = { onPersonClick(person) })
        }
      }
    }
  }
}

@Composable
private fun LoadingText() {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(text = "Loading...", color = TitleColor)
  }
}

@Composable
private fun ErrorText(message: String) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(text = "Failed to load: $message", color = TitleColor, textAlign = TextAlign.Center)
  }
}

@Composable
private fun EmptyText(message: String) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(text = message, color = TitleColor, textAlign = TextAlign.Center)
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun <T> PosterGrid(items: List<T>, itemContent: @Composable (T) -> Unit) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    horizontalArrangement = Arrangement.spacedBy(2.dp),
    verticalArrangement = Arrangement.spacedBy(2.dp)
  ) {
    items(items, key = { it.hashCode() }) { item ->
      itemContent(item)
    }
  }
}

@Composable
private fun PosterGridItem(title: String, posterPath: String?, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(290.dp)
      .clickable(onClick = onClick)
  ) {
    MoviePoster(
      posterUrl = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
      contentDescription = title,
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
        text = title,
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
private fun PosterDetailView(
  title: String,
  overview: String,
  posterPath: String?,
  metadata: String
) {
  Column(modifier = Modifier.fillMaxSize()) {
    Box(modifier = Modifier.fillMaxWidth().height(440.dp)) {
      MoviePoster(
        posterUrl = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
        contentDescription = title,
        modifier = Modifier.fillMaxSize()
      )
    }

    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)) {
      Text(text = metadata, color = TabUnselected, style = MaterialTheme.typography.caption)
      Text(text = title, color = TitleColor, style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
      if (overview.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = overview, color = TitleColor)
      }
    }
  }
}
