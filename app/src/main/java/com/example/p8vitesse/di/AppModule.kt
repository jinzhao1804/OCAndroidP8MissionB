package com.example.p8vitesse.di

import android.content.Context
import com.example.p8vitesse.data.dao.CandidatDtoDao
import com.example.p8vitesse.data.database.AppDatabase
import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.usecase.GetAllCandidatsUseCase
import com.example.p8vitesse.domain.usecase.GetCandidatByIdUseCase
import com.example.p8vitesse.domain.usecase.GetFavorisCandidatsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context, coroutineScope: CoroutineScope): AppDatabase {
        return AppDatabase.getDatabase(context, coroutineScope)
    }


    @Provides
    fun provideCandidatDao(appDatabase: AppDatabase): CandidatDtoDao {
        return appDatabase.candidatDtoDao()
    }


    @Provides
    @Singleton
    fun provideCandidatRepository(userDao: CandidatDtoDao): CandidatRepository {
        return CandidatRepository(userDao)
    }

    @Provides
    fun provideGetAllCandidatsUseCase(candidatRepository: CandidatRepository): GetAllCandidatsUseCase {
        return GetAllCandidatsUseCase(candidatRepository)
    }

    @Provides
    fun provideGetAllFavCandidatsUseCase(candidatRepository: CandidatRepository): GetFavorisCandidatsUseCase {
        return GetFavorisCandidatsUseCase(candidatRepository)
    }

    @Provides
    fun provideGetCandidatByIdUseCase(candidatRepository: CandidatRepository): GetCandidatByIdUseCase {
        return GetCandidatByIdUseCase(candidatRepository)
    }


}