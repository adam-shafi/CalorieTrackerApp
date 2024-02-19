package com.example.calorietracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.MonthDay

@Dao
interface DailyLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyLog(dailyLog: DailyLog)

    @Delete
    suspend fun deleteDailyLog(dailyLog: DailyLog)

    @Query("SELECT * FROM DailyLog WHERE date = :date")
    suspend fun getTodoByDate(date: Long): DailyLog?

    @Query("SELECT * FROM DailyLog")
    fun getAllDailyLog(): Flow<List<DailyLog>>
}