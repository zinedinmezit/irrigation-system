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
                        .addMigrations(MIGRATION_1_2)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        val MIGRATION_1_2 = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "INSERT INTO Day VALUES (1,1,'SUN'),(2,2, 'MON'),(3,3, 'TUE'),(4,4, 'WED'),(5,5, 'THU'),(6,6, 'FRI'),(7,7, 'SAT')"
                )
            }
        }
    }
}