package com.example.apicalldemo.roomDataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.models.ResponseItem

@Dao
interface MovieListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ResponseItem)
    @Update
    fun update(item: ResponseItem)

    @Delete
    fun delete(item: ResponseItem)

    @Query("delete from movieList_model")
    fun deleteMovieItem()

    @Query("select * from movieList_model order by id")
    fun getAllMovies(): LiveData<List<ResponseItem>>
}