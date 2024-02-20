package com.example.calorietracker.di

import android.app.Application
import androidx.room.Room
import com.example.calorietracker.data.DailyLogDatabase
import com.example.calorietracker.data.DailyLogRepository
import com.example.calorietracker.data.DailyLogRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesDailyLogDatabase(app: Application): DailyLogDatabase {
        return Room.databaseBuilder(
            app,
            DailyLogDatabase::class.java,
            "daily_log_db"
        )
//            .fallbackToDestructiveMigration() // for when you feel devious...
            .build()
    }

    @Provides
    @Singleton
    fun providesDailyLogRepository(db: DailyLogDatabase): DailyLogRepository {
        return DailyLogRepositoryImpl(
            dailyLogDao = db.dailyLogDao,
            mealDao = db.mealDao,
            foodDao = db.foodDao,
            nutritionBudgetDao = db.nutritionBudgetDao
        )
    }
}