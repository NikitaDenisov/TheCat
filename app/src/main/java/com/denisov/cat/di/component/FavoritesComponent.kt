package com.denisov.cat.di.component

import android.content.Context
import com.denisov.cat.di.ActivityContext
import com.denisov.cat.di.getAppComponent
import com.denisov.cat.di.module.FavoritesScreenModule
import com.denisov.cat.di.module.LayoutInflaterModule
import com.denisov.cat.di.scope.PerFragment
import com.denisov.cat.presentation.ui.FavoritesFragment
import dagger.BindsInstance
import dagger.Component

@PerFragment
@Component(
    dependencies = [AppComponent::class],
    modules = [LayoutInflaterModule::class, FavoritesScreenModule::class]
)
interface FavoritesComponent {

    fun inject(fragment: FavoritesFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun fragment(fragment: FavoritesFragment): Builder

        @BindsInstance
        fun context(@ActivityContext context: Context): Builder

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): FavoritesComponent
    }
}

fun FavoritesFragment.buildFavoriteComponent() = lazy {
    DaggerFavoritesComponent
        .builder()
        .appComponent(getAppComponent())
        .fragment(this)
        .context(activity!!)
        .build()
}