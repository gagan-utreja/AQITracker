package com.proximity.aqi.database

import androidx.room.TypeConverter
import java.util.*

object UUIDConverter {
    @TypeConverter
    @JvmStatic
    public fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    @JvmStatic
    public fun uuidFromString(string: String?): UUID {
        return UUID.fromString(string)
    }
}