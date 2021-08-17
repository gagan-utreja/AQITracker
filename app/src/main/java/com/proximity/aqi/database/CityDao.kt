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
package com.baruckis.cryptolive.database;

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Defines database operations.
 */
@Dao
interface CityDao {


    @Query("SELECT * FROM city_name_list  ORDER BY city_name ASC")
    fun getCityNameList(): LiveData<List<CityNameEntity>>

    @Query("SELECT * FROM aqi_data where city_name LIKE :search")
    fun getAQIList(search: String): List<ListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCityNameList(city: List<CityNameEntity>)

    @Insert
    fun addAQIDataList(city: List<ListEntity>)

}
