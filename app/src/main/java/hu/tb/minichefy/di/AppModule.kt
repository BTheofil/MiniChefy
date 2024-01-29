package hu.tb.minichefy.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.tb.minichefy.data.data_source.db.RecipeDatabase
import hu.tb.minichefy.data.data_source.memory.RecipeDataSource
import hu.tb.minichefy.data.data_source.memory.RecipeMemoryDataSource
import hu.tb.minichefy.data.repository.RecipeDatabaseRepositoryImpl
import hu.tb.minichefy.data.repository.RecipeMemoryRepositoryImpl
import hu.tb.minichefy.domain.repository.RecipeRepository
import hu.tb.minichefy.domain.use_case.ValidateQuantityNumber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataSource(): RecipeDataSource =
        RecipeMemoryDataSource()

    @Provides
    @Singleton
    fun provideRecipeMemoryRepository(dataSource: RecipeDataSource): RecipeMemoryRepositoryImpl =
        RecipeMemoryRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideTaskDatabase(app: Application): RecipeDatabase =
        Room.databaseBuilder(
            app,
            RecipeDatabase::class.java,
            RecipeDatabase.DATABASE_NAME
        )
            .build()

    @Provides
    @Singleton
    fun provideRecipeDatabaseRepository(database: RecipeDatabase): RecipeRepository =
        RecipeDatabaseRepositoryImpl(database.recipeDao)

    @Provides
    fun provideValidateQuantityNumberUseCase(): ValidateQuantityNumber =
        ValidateQuantityNumber()
}