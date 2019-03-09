package com.data.common

import com.data.password.PasswordRepository
import com.data.password.PasswordRepositoryImpl
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