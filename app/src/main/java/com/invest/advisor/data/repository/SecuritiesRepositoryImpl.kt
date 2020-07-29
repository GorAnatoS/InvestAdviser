package com.invest.advisor.data.repository

import androidx.lifecycle.LiveData
import com.invest.advisor.data.db.SecuritiesDao
import com.invest.advisor.data.db.entity.Securities
import com.invest.advisor.data.db.secList.SecListEntry
import com.invest.advisor.data.network.SecuritiesNetworkDataSource
import com.invest.advisor.data.network.response.SecuritiesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class SecuritiesRepositoryImpl(
    private val securitiesDao: SecuritiesDao,
    private val securitiesNetworkDataSource: SecuritiesNetworkDataSource
) : SecuritiesRepository {

    init {
        securitiesNetworkDataSource.apply {
            downloadedSecurities.observeForever { newSecurities ->
                persistFetchedSecurities(newSecurities)
            }
        }
    }
    override suspend fun getSecuritiesList(): LiveData<List<Securities>> {
        initSecuritiesData()

        return withContext(Dispatchers.IO){
            return@withContext securitiesDao.getRoomSecurities()
        }
    }

    private fun persistFetchedSecurities(fetchedSecurities: SecuritiesResponse){
        GlobalScope.launch(Dispatchers.IO) {
            securitiesDao.upsert(fetchedSecurities.currentSecurities)
        }
    }

    private suspend fun initSecuritiesData() {
        if (isFetchSecuritiesNeeded(ZonedDateTime.now().minusMinutes(10))){
            fetchSecurities()
        }
    }


    // TODO: 7/29/2020 c securities не надо, надо с маркетдата
    private suspend fun fetchSecurities() {
        securitiesNetworkDataSource.fetchSecurities()
    }

    private fun isFetchSecuritiesNeeded(lastFetchTime: ZonedDateTime): Boolean{
        val oneMinuteAgo = ZonedDateTime.now().minusMinutes(1)
        return lastFetchTime.isBefore(oneMinuteAgo)
    }
}