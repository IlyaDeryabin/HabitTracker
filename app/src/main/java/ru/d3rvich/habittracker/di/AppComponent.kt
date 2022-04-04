package ru.d3rvich.habittracker.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    fun featureComponent(): FeatureComponent.Builder

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun applicationContext(context: Context): Builder

        fun build(): AppComponent
    }
}

@Module(includes = [DomainModule::class, DataModule::class],
    subcomponents = [FeatureComponent::class])
object AppModule