package hu.tb.minichefy.domain.model.entity_extentsion

import androidx.room.TypeConverter
import com.google.gson.Gson

class IdListConverter {

    @TypeConverter
    fun convertIdsToJSONString(ids: List<Long>): String = Gson().toJson(ids)

    @TypeConverter
    fun convertStringToFoodIds(jsonString: String): List<Long> =
        Gson().fromJson<List<Long>>(jsonString)

}