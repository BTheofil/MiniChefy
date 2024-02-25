package hu.tb.minichefy.domain.model.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

@Entity
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val title: String,
    val quantity: Int,
    val unitOfMeasurement: UnitOfMeasurement,
    val tagList: FoodTagListWrapper?
)

@Entity
data class FoodTagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val tag: String
)

data class FoodTagListWrapper(
    val foodTagList : List<FoodTagEntity>
)

class RoomTypeConverters{
    @TypeConverter
    fun convertTagListToJSONString(tagList: FoodTagListWrapper): String = Gson().toJson(tagList)
    @TypeConverter
    fun convertJSONStringToTagList(jsonString: String): FoodTagListWrapper = Gson().fromJson(jsonString,FoodTagListWrapper::class.java)

}

