package com.base.injection.module

import android.content.Context
import com.base.imageloader.ImageLoader
import com.base.injection.scope.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * Created by vophamtuananh on 1/13/18.
 */

@Module(includes = [AppContextModule::class])
class ImageLoaderModule {

    @Provides
    @ApplicationScope
    fun provideImageLoader(context: Context): ImageLoader {
        return ImageLoader(context)
    }
}