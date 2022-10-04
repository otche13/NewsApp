package ru.otche13.newsapp.data.api

import ru.otche13.newsapp.data.db.ArticleDao
import ru.otche13.newsapp.models.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService,
    private val articleDao: ArticleDao
    ) {
    suspend fun getNews(countryCode: String, pageNumber: Int) =
        newsService.getHeadlines(countryCode = countryCode, page = pageNumber)

    fun getFavoriteArticles() = articleDao.getAllArticles()

    suspend fun addToFavotrite(article: Article) = articleDao.insert(article = article)
}