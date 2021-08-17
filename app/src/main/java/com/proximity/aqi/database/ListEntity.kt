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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.*

/**
 * Data class for Location related data (only takes what's needed from
 * {@link android.location.Location} class).
 */
@Entity(
    tableName = "aqi_data"
)
data class ListEntity(
    @PrimaryKey
    @NotNull var id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "city_name")
    var cityName: String,
    @ColumnInfo(name = "aqi")
    var aqi: Float,
    @ColumnInfo(name = "created_at")
    var createdAt: Long = 0
)
