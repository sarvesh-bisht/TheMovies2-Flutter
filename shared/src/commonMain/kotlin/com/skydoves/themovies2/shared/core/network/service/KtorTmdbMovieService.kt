package com.skydoves.themovies2.shared.core.network.service

import com.skydoves.themovies2.shared.core.network.model.TmdbMovieListDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorTmdbMovieService(
  private val client: HttpClient,
  private val apiKey: String,
  private val baseUrl: String = "https://api.themoviedb.org/3"
) : TmdbMovieService {

  override suspend fun fetchPopularMovies(page: Int): TmdbMovieListDto {
    return client.get("$baseUrl/movie/popular") {
      parameter("api_key", apiKey)
      parameter("page", page)
    }.body()
  }
}
