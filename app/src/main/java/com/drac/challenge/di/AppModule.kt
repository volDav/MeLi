package com.drac.challenge.di

import com.drac.challenge.data.impl.DataRepositoryImpl
import com.drac.challenge.domain.repository.DataRepository
import com.drac.challenge.network.MeliApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewComponent
import javax.inject.Singleton


@Module
@InstallIn(ViewComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataRepository(api: MeliApi): DataRepository {
        return DataRepositoryImpl(api)
    }

}