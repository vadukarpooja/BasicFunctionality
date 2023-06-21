package com.example.apicalldemo

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.apicalldemo.models.ColorsModel

@Database(entities = [ColorsModel::class], version = 6)
abstract class ColorDataBase:RoomDatabase() {
    abstract fun colorDao(): ColorDao


    companion object {

        private var instance: ColorDataBase? = null

        @Synchronized
        fun getInstance(ctx: Context): ColorDataBase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx, ColorDataBase::class.java,
                    "colors_model"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.e(javaClass.simpleName, ": $db", )
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: ColorDataBase) {
            val colorDao = db.colorDao()
            subscribeOnBackground {
                colorDao.insert(ColorsModel(id ="0", name = "color1", year = "2000", color = "#ffff"))
                colorDao.insert(ColorsModel(id ="0", name = "color2", year = "2001", color = "#0000"))
                colorDao.insert(ColorsModel(id ="0", name = "color3", year = "2002", color = "#0000"))
                colorDao.insert((ColorsModel(id ="0", name = "color4", year = "2003", color = "#ffff")))
            }
        }
    }
}