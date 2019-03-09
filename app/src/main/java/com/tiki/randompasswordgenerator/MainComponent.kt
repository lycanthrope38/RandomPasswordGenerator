package com.tiki.randompasswordgenerator

import com.base.injection.scope.OutOfApplicationScope
import com.data.common.AppComponent
import dagger.Component

@Component(dependencies = [(AppComponent::class)])
@OutOfApplicationScope
interface MainComponent {
    fun inject(i: MainActivity)
}