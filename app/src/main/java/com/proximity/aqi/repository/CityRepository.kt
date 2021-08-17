package com.proximity.aqi.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.baruckis.cryptolive.database.CityNameEntity
import com.baruckis.cryptolive.database.ListEntity
import com.proximity.aqi.database.AppDatabase
import com.proximity.aqi.sockets.models.Results
import java.util.*
import java.util.concurrent.ExecutorService

class CityRepository private constructor(
    private val mDatabase: AppDatabase,
    private val executor: ExecutorService
) {

    private val cityDao = mDatabase.cityDao()

    /**
     * Insert city list for current AQI value.
     */
    fun insertCityNameList(cityNameList: ArrayList<Results>) {
        val cityNameList = cityNameList.map { cityName ->
            CityNameEntity(
                cityName = cityName.city,
                aqi = cityName.aqi,
                updatedAt = Date().time
            )

        }
        executor.execute {
            cityDao?.addCityNameList(cityNameList)
        }
    }


    /**
     * Insert city list for history AQI value.
     */
    fun insertList(cityNameList: ArrayList<Results>) {
        val cityNameList = cityNameList.map { cityName ->
            ListEntity(
                cityName = cityName.city,
                aqi = cityName.aqi,
                createdAt = Date().time
            )

        }
        executor.execute {
            cityDao?.addAQIDataList(cityNameList)
        }
    }

    /**
     * Returns all recorded city with current AQI from database.
     */
    fun getCitName(): LiveData<List<CityNameEntity>>? = cityDao?.getCityNameList()

    /**
     * Returns all recorded city history from database.
     */
    fun getCityHistory(search: String): List<ListEntity>? = cityDao?.getAQIList(search)

    companion object {
        @Volatile
        private var INSTANCE: CityRepository? = null

        fun getInstance(context: Context, executor: ExecutorService): CityRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CityRepository(
                    AppDatabase.getInstance(context),
                    executor
                )
                    .also { INSTANCE = it }
            }
        }
    }
}