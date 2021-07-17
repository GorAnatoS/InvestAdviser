package com.invest.advisor

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.invest.advisor.data.db.database.moex.MoexDatabase
import com.invest.advisor.data.network.*
import com.invest.advisor.data.network.moexResponse.MoexApiService
import com.invest.advisor.data.network.moexResponse.MoexNetworkDataSource
import com.invest.advisor.data.network.moexResponse.MoexNetworkDataSourceImpl
import com.invest.advisor.data.network.yahooResponse.YahooApiService
import com.invest.advisor.data.network.yahooResponse.YahooNetworkDataSource
import com.invest.advisor.data.network.yahooResponse.YahooNetworkDataSourceImpl
import com.invest.advisor.data.repository.MoexRepository
import com.invest.advisor.data.repository.MoexRepositoryImpl
import com.invest.advisor.ui.moexsecurities.MoexViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*

/*
Application class where we set up:
dependency injection with KodeIn,
TimeSupport with AndroidThreeTen
And temporally showing only DAY_MODE
*/

//All links:
//http://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities/AFKS.xml?iss.meta=off&iss.only=securities&securities.columns=SECID,SECNAME,LATNAME
//http://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities/AFKS.json?iss.meta=off&iss.only=securities,marketdata
//http://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities.json?iss.meta=off&iss.only=securities&securities.columns=SECID
//https://query1.finance.yahoo.com/v10/finance/quoteSummary/YNDX.ME?modules=assetProfile%2CfinancialData%2Cprice

class InvestAdviserApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@InvestAdviserApplication))

        bind() from singleton { MoexDatabase(instance()) }
        bind() from singleton { instance<MoexDatabase>().moexDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { MoexApiService(instance()) }
        bind<MoexNetworkDataSource>() with singleton { MoexNetworkDataSourceImpl(instance()) }
        bind<MoexRepository>() with singleton { MoexRepositoryImpl(instance(), instance()) }
        bind() from provider { MoexViewModelFactory(instance()) }

        bind() from singleton { YahooApiService(instance()) }
        bind<YahooNetworkDataSource>() with singleton { YahooNetworkDataSourceImpl(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}