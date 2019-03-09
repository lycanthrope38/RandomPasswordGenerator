package com.data.common

import com.base.injection.module.AppContextModule
import dagger.Module


@Module(includes = [(AppContextModule::class)])
class AppModule