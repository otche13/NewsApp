package ru.otche13.newsapp.data.api


import ru.otche13.newsapp.data.db.WebItemDao
import ru.otche13.newsapp.models.WebItem
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService,
    private val webItemDao: WebItemDao
    ) {
    suspend fun getNews(countryCode: String, pageNumber: Int) =
        newsService.getHeadlines(countryCode = countryCode, page = pageNumber)

//    fun getWebItem(id:Int) = webItemDao.getAllWebsItem(id = id)

    suspend fun addWebItem(webItem: WebItem) = webItemDao.insert(webItem=webItem )
}