package com.skydoves.themovies2.shared.core.domain.usecase

import com.skydoves.themovies2.shared.core.domain.model.PersonSummary
import com.skydoves.themovies2.shared.core.domain.repository.MovieRepository

class GetPopularPeopleUseCase(
  private val movieRepository: MovieRepository
) {
  suspend operator fun invoke(page: Int = 1): List<PersonSummary> {
    return movieRepository.getPopularPeople(page)
  }
}
