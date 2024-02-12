package hu.tb.minichefy.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.tb.minichefy.data.data_source.recipe.RecipeDatabase
import hu.tb.minichefy.data.data_source.storage.StorageDatabase
import hu.tb.minichefy.data.repository.RecipeDatabaseRepositoryImpl
import hu.tb.minichefy.data.repository.StorageDatabaseRepositoryImpl
import hu.tb.minichefy.domain.repository.RecipeRepository
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.ValidateQuantityNumber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // recipe db
    @Provides
    @Singleton
    fun provideRecipeDatabase(app: Application): RecipeDatabase =
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


    // storage db
    @Provides
    @Singleton
    fun provideStorageDatabase(
        app: Application
    ): StorageDatabase =
        Room.databaseBuilder(
            app,
            StorageDatabase::class.java,
            StorageDatabase.DATABASE_NAME
        )
            .build()

    @Provides
    @Singleton
    fun provideStorageDatabaseRepository(database: StorageDatabase): StorageRepository =
        StorageDatabaseRepositoryImpl(database.storageDao)


    // use case
    @Provides
    fun provideValidateQuantityNumberUseCase(): ValidateQuantityNumber =
        ValidateQuantityNumber()
}