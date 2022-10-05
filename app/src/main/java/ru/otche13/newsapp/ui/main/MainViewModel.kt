package ru.otche13.newsapp.ui.main

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

    init {
        getNews("ru")
    }

    private val _webData = MutableLiveData<List<WebItem>>()
    val webData: LiveData<List<WebItem>> = _webData



    val db= Firebase.firestore

    init {

//        getDataFirebase(_webData)
//        Log.i("getDataFirebase(_webData)","${getDataFirebase(_webData)}")
    }

//    fun getWebItem(id:Int) = viewModelScope.launch(Dispatchers.IO) {
//        repository.getWebItem(id)
//    }

    fun saveWebItem(id: Int, urlAdress:String) = viewModelScope.launch(Dispatchers.IO) {
        val webItem = WebItem(id = id, urlAdress = urlAdress)
        repository.addWebItem(webItem)
        Log.i("fdfsdfsf", "$webItem")

    }

//    fun getUrl():String{
//        val urlItem=getWebItem()
//        return urlItem.toString()
//    }

//    fun getDataFirebase(webItemList: MutableLiveData<List<WebItem>>) {
//        val _webItemsList= arrayListOf<WebItem>()
//        db.collection("webItemfb")
//            .get()
//            .addOnSuccessListener {
//                if(!it.isEmpty){
//                    for (data in it.documents){
//                        val webItem: WebItem? =data.toObject(WebItem::class.java)
//                        if (webItem!=null){
//                            _webItemsList.add(webItem)
//
//                        }
//                    }
//                    webItemList.postValue(_webItemsList)
//                }
//            }
//    }

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

}