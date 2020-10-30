package com.example.irrigationsystem.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.irrigationsystem.models.*

@Database(entities = [Plan::class,
                    NotificationScheduler::class,
                    WateringScheduler::class,
                    Day::class,
                    WateringSchedulerDays::class],
            views = [
                PlanWateringSchedulerView::class,
                ScheduledDaysView::class
            ],
            version = 2,
            exportSchema = false)
abstract class IrrigationSystemDatabase : RoomDatabase() {
    abstract val IrrigationDatabaseDao : IrrigationDao

    companion object{

        @Volatile
        private var INSTANCE : IrrigationSystemDatabase? = null

        fun getInstance(context : Context) : IrrigationSystemDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                    IrrigationSystemDatabase::class.java,
                    "irrigation_system_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}