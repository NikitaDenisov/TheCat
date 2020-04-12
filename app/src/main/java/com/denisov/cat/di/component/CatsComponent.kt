package com.denisov.cat.di.component

import android.content.Context
import androidx.fragment.app.Fragment
import com.denisov.cat.di.ActivityContext
import com.denisov.cat.di.getAppComponent
import com.denisov.cat.di.module.CatsScreenModule
import com.denisov.cat.di.module.LayoutInflaterModule
import com.denisov.cat.di.scope.PerFragment
import com.denisov.cat.presentation.ui.CatsFragment
import dagger.BindsInstance
import dagger.Component

@PerFragment
@Component(
    dependencies = [AppComponent::class],
    modules = [LayoutInflaterModule::class, CatsScreenModule::class]
)
interface CatsComponent {

    fun inject(fragment: CatsFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun fragment(fragment: Fragment): Builder

        @BindsInstance
        fun context(@ActivityContext context: Context): Builder

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): CatsComponent
    }
}

fun CatsFragment.buildCatsComponent() = lazy {
    DaggerCatsComponent
        .builder()
        .appComponent(getAppComponent())
        .fragment(this)
        .context(activity!!)
        .build()
}