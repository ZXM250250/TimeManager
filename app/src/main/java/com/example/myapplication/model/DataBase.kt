package com.example.myapplication.model


import android.content.Context
import androidx.room.*



@Database(entities = [TaskEntity::class ],version = 1)
abstract class DataBase : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: DataBase? = null

        fun getDBInstace(context: Context): DataBase {

            if (instance == null) {

                synchronized(DataBase::class) {

                    if (instance == null) {

                        instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            DataBase::class.java,
                            "Task.db"
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return instance!!
        }
    }


    abstract fun getTaskDao():TaskDao
}