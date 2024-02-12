package hu.tb.minichefy.domain.model.storage.entity

import androidx.room.TypeConverter
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

class UnitOfMeasurementConverter {
    @TypeConverter
    fun unitToInt(unit: UnitOfMeasurement): Int = unit.id

    @TypeConverter
    fun intToUnit(id: Int): UnitOfMeasurement = when (id) {
        1 -> UnitOfMeasurement.DL
        2 -> UnitOfMeasurement.L
        3 -> UnitOfMeasurement.G
        4 -> UnitOfMeasurement.DKG
        5 -> UnitOfMeasurement.KG
        else -> throw Exception()
    }
}