package com.example.apicalldemo

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.apicalldemo.models.ResponseItem


@Database(entities = [ResponseItem::class], version = 5, exportSchema = false)
/*@TypeConverters(ConvertersImageList.class)*/
abstract class MovieListDatabase : RoomDatabase() {
        abstract fun movieListDao(): MovieListDao
        companion object {

            private var instance: MovieListDatabase? = null

            @Synchronized
            fun getInstance(ctx: Context): MovieListDatabase {
                if (instance == null)
                    instance = Room.databaseBuilder(
                        ctx, MovieListDatabase::class.java,
                        "movieList_model"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()

                return instance!!

            }

            private val roomCallback = object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.e(javaClass.simpleName, ": $db")
                    populateDatabase(instance!!)
                }
            }

            private fun populateDatabase(db: MovieListDatabase) {
                val movieListDao = db.movieListDao()
                subscribeOnBackground {

                }
            }
        }
}