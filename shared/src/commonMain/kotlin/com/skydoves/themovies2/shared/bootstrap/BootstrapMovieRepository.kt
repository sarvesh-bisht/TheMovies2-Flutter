package com.skydoves.themovies2.shared.bootstrap

import com.skydoves.themovies2.shared.core.domain.model.MovieSummary
import com.skydoves.themovies2.shared.core.domain.model.PersonSummary
import com.skydoves.themovies2.shared.core.domain.model.TvSummary
import com.skydoves.themovies2.shared.core.domain.repository.MovieRepository

class BootstrapMovieRepository : MovieRepository {
  override suspend fun getPopularMovies(page: Int): List<MovieSummary> {
    return listOf(
      MovieSummary(
        id = 603,
        title = "The Matrix",
        overview = "A computer hacker learns about the true nature of reality.",
        posterPath = "/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg"
      ),
      MovieSummary(
        id = 680,
        title = "Pulp Fiction",
        overview = "Stories of crime intertwine in Los Angeles.",
        posterPath = "/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg"
      )
    )
  }

  override suspend fun getPopularTvs(page: Int): List<TvSummary> {
    return listOf(
      TvSummary(
        id = 1399,
        name = "Game of Thrones",
        overview = "Nine noble families wage war against each other.",
        posterPath = "/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg"
      ),
      TvSummary(
        id = 1396,
        name = "Breaking Bad",
        overview = "A chemistry teacher turns to making methamphetamine.",
        posterPath = "/ztkUQFLlC19CCMYHW9o1zWhJRNq.jpg"
      )
    )
  }

  override suspend fun getPopularPeople(page: Int): List<PersonSummary> {
    return listOf(
      PersonSummary(
        id = 287,
        name = "Brad Pitt",
        knownForDepartment = "Acting",
        profilePath = "/kU3B75TyRiCgE270EyZnHjfivoq.jpg"
      ),
      PersonSummary(
        id = 500,
        name = "Tom Cruise",
        knownForDepartment = "Acting",
        profilePath = "/8qBylBsQf4llkGrWR3qAsOtOU8O.jpg"
      )
    )
  }
}
