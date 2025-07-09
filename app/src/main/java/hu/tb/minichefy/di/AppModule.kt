package hu.tb.minichefy.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.tb.minichefy.data.data_source.db.MiniChefyDatabase
import hu.tb.minichefy.data.repository.RecipeDatabaseRepositoryImpl
import hu.tb.minichefy.data.repository.StorageDatabaseRepositoryImpl
import hu.tb.minichefy.domain.model.storage.entity.TagEntity
import hu.tb.minichefy.domain.repository.RecipeRepository
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.CalculateMeasurements
import hu.tb.minichefy.presentation.util.DataStoreManager
import hu.tb.minichefy.domain.use_case.ValidateQuantity
import hu.tb.minichefy.domain.use_case.ValidateNumberKeyboard
import hu.tb.minichefy.domain.use_case.ValidateTextField
import hu.tb.minichefy.domain.use_case.Validators
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

const val DISH_TAG_ID = 3

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

        /*val dbFile = app.applicationContext.getDatabasePath(MiniChefyDatabase.DATABASE_NAME)
        if (!dbFile.exists()) {
            CoroutineScope(Dispatchers.IO).launch {
                db.storageDao.insertTagEntity(
                    TagEntity(tagId = 1, tag = "fruit")
                )
                db.storageDao.insertTagEntity(
                    TagEntity(tagId = 2, tag = "vegetable")
                )
                db.storageDao.insertTagEntity(
                    TagEntity(tagId = DISH_TAG_ID.toLong(), tag = "dish")
                )
            }
        }*/

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
    @Singleton
    @Provides
    fun provideValidatorsUseCase(): Validators =
        Validators(
            validateQuantity = ValidateQuantity(),
            validateNumberKeyboard = ValidateNumberKeyboard(),
            validateTextField = ValidateTextField()
        )

    @Provides
    fun provideCalculateMeasurements(): CalculateMeasurements =
        CalculateMeasurements()

    @Provides
    @Singleton
    fun provideDataStore(app: Application): DataStoreManager =
        DataStoreManager(app)
}

