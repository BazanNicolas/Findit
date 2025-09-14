package com.products.app.di

import com.products.app.data.repository.AutosuggestRepositoryImpl
import com.products.app.domain.repository.AutosuggestRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AutosuggestModule {

    @Binds
    abstract fun bindAutosuggestRepository(
        autosuggestRepositoryImpl: AutosuggestRepositoryImpl
    ): AutosuggestRepository
}
