package hu.tb.minichefy.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.tb.minichefy.data.data_source.RecipeDataSource
import hu.tb.minichefy.data.data_source.RecipeMemoryDataSource
import hu.tb.minichefy.data.repository.RecipeRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataSource(): RecipeDataSource{
        return RecipeMemoryDataSource()
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(dataSource: RecipeDataSource): RecipeRepositoryImpl{
        return RecipeRepositoryImpl(dataSource)
    }
}