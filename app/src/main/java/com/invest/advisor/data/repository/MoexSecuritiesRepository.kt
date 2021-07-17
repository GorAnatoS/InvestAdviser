package com.invest.advisor.data.repository

import androidx.lifecycle.LiveData
import com.invest.advisor.data.db.database.moex.MoexDatabaseDao
import com.invest.advisor.data.db.entity.MarketData
import com.invest.advisor.data.db.entity.Securities
import com.invest.advisor.data.network.moexResponse.MoexNetworkDataSource
import com.invest.advisor.data.network.moexResponse.MarketDataResponse
import com.invest.advisor.data.network.moexResponse.SecuritiesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

/**
 * Implementation of [MoexSecuritiesRepositoryInterface]
 */
class MoexSecuritiesRepository(
    private val moexDatabaseDao: MoexDatabaseDao,
    private val moexNetworkDataSource: MoexNetworkDataSource
) : MoexSecuritiesRepositoryInterface {

    init {
        moexNetworkDataSource.apply {
            downloadedSecurities.observeForever { newSecurities ->
                persistFetchedSecurities(newSecurities)
            }

            downloadedMarketData.observeForever { newMarketData ->
                persistFetchedMarketData(newMarketData)
            }
        }
    }

    override suspend fun getMarketData(): LiveData<List<MarketData>> {
        initMarketData()

        return withContext(Dispatchers.IO) {
            return@withContext moexDatabaseDao.getRoomMarketData()
        }
    }

    override suspend fun getSecurities(): LiveData<List<Securities>> {
        initSecuritiesData()

        return withContext(Dispatchers.IO) {
            return@withContext moexDatabaseDao.getRoomSecurities()
        }
    }

    private fun persistFetchedSecurities(fetchedSecurities: SecuritiesResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            moexDatabaseDao.upsert(fetchedSecurities.currentSecurities)
        }
    }

    private suspend fun initSecuritiesData() {
        if (isFetchSecuritiesNeeded(ZonedDateTime.now().minusMinutes(10))) {
            fetchSecurities()
        }
    }

    private suspend fun fetchSecurities() {
        moexNetworkDataSource.fetchSecurities()
    }

    private fun isFetchSecuritiesNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val oneMinuteAgo = ZonedDateTime.now().minusMinutes(1)
        return lastFetchTime.isBefore(oneMinuteAgo)
    }

    private fun persistFetchedMarketData(fetchedMarketData: MarketDataResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            moexDatabaseDao.upsert(fetchedMarketData.currentMarketData)
        }
    }

    private suspend fun initMarketData() {
        if (isFetchMarketDataNeeded(ZonedDateTime.now().minusMinutes(10))) {
            fetchMarketData()
        }
    }

    private suspend fun fetchMarketData() {
        moexNetworkDataSource.fetchMarketData()
    }

    //update data every minute
    private fun isFetchMarketDataNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val oneMinuteAgo = ZonedDateTime.now().minusMinutes(1)
        return lastFetchTime.isBefore(oneMinuteAgo)
    }
}

/**
 * MoexRepository interface
 */
interface MoexSecuritiesRepositoryInterface {
    suspend fun getMarketData(): LiveData<List<MarketData>>
    suspend fun getSecurities(): LiveData<List<Securities>>
}