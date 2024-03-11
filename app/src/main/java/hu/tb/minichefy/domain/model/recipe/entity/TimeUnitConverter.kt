package hu.tb.minichefy.domain.model.recipe.entity

import androidx.room.TypeConverter
import hu.tb.minichefy.domain.model.recipe.TimeUnit

class TimeUnitConverter {

    @TypeConverter
    fun unitToInt(unit: TimeUnit): Int = unit.id

    @TypeConverter
    fun intToUnit(id: Int): TimeUnit = when (id) {
        1 -> TimeUnit.MINUTES
        2 -> TimeUnit.HOUR
        else -> throw Exception()
    }

}