package hu.tb.minichefy.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.tb.minichefy.data.data_source.dao.UNKNOWN_TAG_ID
import hu.tb.minichefy.data.data_source.db.MiniChefyDatabase
import hu.tb.minichefy.data.repository.RecipeDatabaseRepositoryImpl
import hu.tb.minichefy.data.repository.StorageDatabaseRepositoryImpl
import hu.tb.minichefy.domain.model.storage.entity.TagEntity
import hu.tb.minichefy.domain.repository.RecipeRepository
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.CalculateMeasurements
import hu.tb.minichefy.domain.use_case.DataStoreManager
import hu.tb.minichefy.domain.use_case.ValidateQuantity
import hu.tb.minichefy.domain.use_case.ValidateCountInteger
import hu.tb.minichefy.domain.use_case.ValidateTextField
import hu.tb.minichefy.presentation.screens.manager.icons.IconManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // recipe db
    @Provides
    @Singleton
    fun provideRecipeDatabase(app: Application): MiniChefyDatabase {
        val db = Room.databaseBuilder(
            app,
            MiniChefyDatabase::class.java,
            MiniChefyDatabase.DATABASE_NAME
        )
            .build()

        val dbFile = app.applicationContext.getDatabasePath(MiniChefyDatabase.DATABASE_NAME)
        if (!dbFile.exists()) {
            CoroutineScope(Dispatchers.IO).launch {
                db.storageDao.insertTagEntity(
                    TagEntity(tagId = 1, tag = "fruit")
                )
                db.storageDao.insertTagEntity(
                    TagEntity(tagId = 2, tag = "vegetable")
                )
                db.storageDao.insertTagEntity(
                    TagEntity(tagId = 3, tag = "dish")
                )
                db.storageDao.insertTagEntity(
                    TagEntity(tagId = UNKNOWN_TAG_ID.toLong(), tag = "unknown")
                )
            }
        }

        return db
    }


    @Provides
    @Singleton
    fun provideRecipeDatabaseRepository(database: MiniChefyDatabase): RecipeRepository =
        RecipeDatabaseRepositoryImpl(database.recipeDao)

    @Provides
    @Singleton
    fun provideStorageDatabaseRepository(database: MiniChefyDatabase): StorageRepository =
        StorageDatabaseRepositoryImpl(database.storageDao)


    // use case
    @Provides
    fun provideValidateCountIntegerUseCase(): ValidateCountInteger =
        ValidateCountInteger()

    @Provides
    fun provideValidateQuantityFloatUseCase(): ValidateQuantity =
        ValidateQuantity()

    @Provides
    fun provideValidateTextFieldUseCase(): ValidateTextField =
        ValidateTextField()

    @Provides
    fun provideCalculateMeasurements(): CalculateMeasurements =
        CalculateMeasurements()

    @Provides
    @Singleton
    fun provideDataStore(app: Application): DataStoreManager =
        DataStoreManager(app)

    @Provides
    fun provideIconManager(): IconManager = IconManager()
}

