package ru.otche13.newsapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.otche13.newsapp.models.WebItem

@Database(entities = [WebItem::class], version = 1, exportSchema = true)
abstract class WebDatabase: RoomDatabase() {
    abstract fun getWebItemDao(): WebItemDao

}