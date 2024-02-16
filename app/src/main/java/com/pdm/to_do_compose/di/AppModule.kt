package com.pdm.to_do_compose.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.pdm.to_do_compose.data.preferences.DefaultPreferences
import com.pdm.to_do_compose.domain.preferences.Preferences
import com.pdm.to_do_compose.util.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideSlimeDispatchers(): AppDispatchers {
        return AppDispatchers(
            default = Dispatchers.Default,
            main = Dispatchers.Main,
            io = Dispatchers.IO
        )
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(
        app: Application
    ): SharedPreferences {
        return app.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferences(
        sharedPreferences: SharedPreferences
    ): Preferences {
        return DefaultPreferences(
            sharedPreferences
        )
    }
}