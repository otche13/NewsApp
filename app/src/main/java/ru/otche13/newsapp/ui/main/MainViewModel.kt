package ru.otche13.newsapp.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.otche13.newsapp.data.api.NewsRepository
import ru.otche13.newsapp.models.NewsResponse
import ru.otche13.newsapp.models.WebItem
import ru.otche13.newsapp.utils.Resource
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: NewsRepository): ViewModel() {

    val newsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var newsPage = 1

    private val _webData = MutableLiveData<List<WebItem>>()
    val webData: LiveData<List<WebItem>> = _webData

    private val _webDataRoom = MutableLiveData<List<WebItem>>()
    val webDataRoom: LiveData<List<WebItem>> = _webDataRoom

    val db= Firebase.firestore

    init {
        getWebItem()
        getDataFirebase(_webData)
        getNews("ru")
    }

     fun getWebItem() {
         viewModelScope.launch(Dispatchers.IO) {
             val res = repository.getWebItem()
             println("DB size: ${res}")
             repository.getWebItem()
             _webDataRoom.postValue(res)
         }
    }

    fun saveWebItem(id: Int, url:String ) = viewModelScope.launch(Dispatchers.IO) {
        val webItem=WebItem(
            id = id,
            url = url
        )
        Log.i("saveWebItem","$webItem")
        repository.addWebItem(webItem)
    }

    fun getDataFirebase(webItemList: MutableLiveData<List<WebItem>>) {
        val _webItemsList= arrayListOf<WebItem>()
        db.collection("webItem")
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    for (data in it.documents){
                        val webItem: WebItem? =data.toObject(WebItem::class.java)
                        if (webItem!=null){
                            _webItemsList.add(webItem)
                        }
                    }
                    webItemList.postValue(_webItemsList)
                }
            }
    }

    private fun getNews(countryCode: String) =
        viewModelScope.launch {
            newsLiveData.postValue(Resource.Loading())
            val response = repository.getNews(countryCode = countryCode, pageNumber = newsPage)
            if (response.isSuccessful) {
                response.body().let { res ->
                    newsLiveData.postValue(Resource.Success(res))
                }
            } else {
                newsLiveData.postValue(Resource.Error(message = response.message()))
            }
        }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}