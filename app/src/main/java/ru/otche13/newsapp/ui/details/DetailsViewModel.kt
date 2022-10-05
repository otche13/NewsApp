package ru.otche13.newsapp.ui.details

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
import ru.otche13.newsapp.models.WebItem
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val  repository: NewsRepository): ViewModel() {

}