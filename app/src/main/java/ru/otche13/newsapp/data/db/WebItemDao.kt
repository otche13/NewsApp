package ru.otche13.newsapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.otche13.newsapp.models.WebItem


@Dao
interface WebItemDao {


    @Query("SELECT * FROM webItem ")
    suspend fun getAllWebsItem():List<WebItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(webItem: WebItem)

}