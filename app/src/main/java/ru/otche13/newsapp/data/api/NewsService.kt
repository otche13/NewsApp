package ru.otche13.newsapp.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.otche13.newsapp.models.NewsResponse
import ru.otche13.newsapp.utils.Constants.Companion.API_KEY

interface NewsService {

    @GET("/v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country") countryCode: String = "ru",
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ) : Response<NewsResponse>
}