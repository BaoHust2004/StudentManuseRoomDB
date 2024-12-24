package com.kot104.baitapbuoi13.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StudentEntity::class], version = 1, exportSchema = false)
abstract class StudentsDB : RoomDatabase() {

    abstract fun studentDao(): StudentDAO

    companion object {

        @Volatile
        private var INTANCE: StudentsDB? = null

        fun getIntance(context: Context): StudentsDB {
            synchronized(this){
                var intance = INTANCE
                if (intance == null){
                    intance = Room.databaseBuilder(
                        context.applicationContext,
                        StudentsDB::class.java,
                        "students_db2"
                    ).build()
                }
                return intance
            }

        }

    }

}