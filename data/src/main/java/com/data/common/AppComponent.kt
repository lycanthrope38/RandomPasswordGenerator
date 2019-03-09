package com.data.common

import com.data.word.PasswordRepository
import dagger.Component
import javax.inject.Singleton

@Component(modules = [(RepositoryModule::class), (AppModule::class)])
@Singleton
interface AppComponent {
    fun passwordRepository(): PasswordRepository
}