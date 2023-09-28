package com.example.apicalldemo.roomDataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.apicalldemo.models.ColorsModel

@Dao
interface ColorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(color: ColorsModel)

    @Update
    fun update(color: ColorsModel)

    @Delete
    fun delete(color: ColorsModel)

    @Query("delete from colors_model")
    fun deleteAllNotes()

    @Query("select * from colors_model order by id")
    fun getAllNotes(): LiveData<List<ColorsModel>>
}