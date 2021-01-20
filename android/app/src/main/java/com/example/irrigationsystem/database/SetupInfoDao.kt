package com.example.irrigationsystem.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.irrigationsystem.models.SetupInfo

@Dao
interface SetupInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetupInfo(setupInfo: SetupInfo)

    @Query("SELECT * FROM SetupInfo LIMIT 1")
    suspend fun getSetupInfo() : SetupInfo

    @Query("UPDATE SetupInfo SET " +
            "IpAddress=:address," +
            "City=:city" )
    suspend fun updateSetupInfo(address : String,
                                city : String,
    )

    @Query("UPDATE SetupInfo SET " +
            "TemperatureMinLimit=:tempMinValue," +
            "TemperatureMaxLimit=:tempMaxValue," +
            "HummidityMinLimit=:hummMinValue," +
            "HummidityMaxLimit=:hummMaxValue")
    suspend fun updateParameterValues(
        tempMinValue : Double,
        tempMaxValue : Double,
        hummMinValue : Double,
        hummMaxValue : Double)

    @Query("SELECT * FROM SetupInfo")
    fun getSetupInfoLiveData() : LiveData<SetupInfo>
}