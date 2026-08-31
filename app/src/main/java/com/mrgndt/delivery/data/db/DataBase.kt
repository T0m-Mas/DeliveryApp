package com.mrgndt.delivery.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        LocationDB::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class MainDataBase : RoomDatabase(
) {
    abstract fun dao(): MainRepositoryDAO

    companion object {
        @Volatile
        private var Instance: MainDataBase? = null

        fun getDatabase(context: Context): MainDataBase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MainDataBase::class.java, "MainDataBase")
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }

    }

}

