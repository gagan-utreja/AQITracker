/*
 * Copyright (C) 2020 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.proximity.aqi.database;

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.baruckis.cryptolive.database.CityDao
import com.baruckis.cryptolive.database.CityNameEntity
import com.baruckis.cryptolive.database.ListEntity

private const val DATABASE_NAME = "aqi-database"

/**
 * Database for storing all location data.
 */
@Database(entities = [CityNameEntity::class, ListEntity::class], version = 1)
@TypeConverters(UUIDConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao?

    companion object {
        // For Singleton instantiation
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}
