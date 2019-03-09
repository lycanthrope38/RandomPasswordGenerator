package com.data.common

import com.data.word.PasswordRepository
import com.data.word.PasswordRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun passwordRepository(): PasswordRepository {
        return PasswordRepositoryImpl()
    }

}