package com.pdm.to_do_compose.di

import com.pdm.to_do_compose.data.repositories.ToDoRepositoryImpl
import com.pdm.to_do_compose.domain.repositories.ToDoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {

    @Binds
    abstract fun provideToDoRepository(
        toDoRepositoryImpl: ToDoRepositoryImpl
    ): ToDoRepository
}