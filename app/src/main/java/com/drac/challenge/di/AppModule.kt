package com.drac.challenge.di

import com.drac.challenge.data.impl.DataRepositoryImpl
import com.drac.challenge.domain.repository.DataRepository
import com.drac.challenge.data.network.MeliApi
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
    fun provideDataRepository(api: MeliApi): DataRepository {
        return DataRepositoryImpl(api)
    }

}