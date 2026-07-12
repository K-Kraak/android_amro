package com.amro.app.di

import com.amro.core.common.time.Clock
import com.amro.core.common.time.SystemClock
import com.amro.core.dispatchers.DefaultDispatcherProvider
import com.amro.core.dispatchers.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreBindingsModule {

    @Binds
    @Singleton
    abstract fun bindClock(
        implementation: SystemClock,
    ): Clock

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(
        implementation: DefaultDispatcherProvider,
    ): DispatcherProvider
}