package ru.otche13.newsapp.ui.details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.otche13.newsapp.data.api.NewsRepository
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val  repository: NewsRepository): ViewModel() {


}