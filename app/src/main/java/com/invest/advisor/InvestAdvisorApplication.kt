package com.invest.advisor

import android.app.Application
import com.invest.advisor.data.db.MoexDatabase
import com.invest.advisor.data.network.*
import com.invest.advisor.data.network.moexResponse.MoexApiService
import com.invest.advisor.data.network.yahooResponse.YahooApiService
import com.invest.advisor.data.repository.MoexRepository
import com.invest.advisor.data.repository.MoexRepositoryImpl
import com.invest.advisor.ui.moex.MoexViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*


/**
 * Created by qsufff on 7/29/2020.
 */


class InvestAdvisorApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@InvestAdvisorApplication))

        bind() from singleton { MoexDatabase(instance()) }
        bind() from singleton { instance<MoexDatabase>().moexDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { MoexApiService(instance()) }
        bind<MoexNetworkDataSource>() with singleton { MoexNetworkDataSourceImpl(instance()) }
        bind<MoexRepository>() with singleton { MoexRepositoryImpl(instance(), instance()) }
        bind() from provider { MoexViewModelFactory(instance()) }

        bind() from singleton { YahooApiService(instance()) }
        bind<YahooNetworkDataSource>() with singleton {YahooNetworkDataSourceImpl(instance()) }
        //bind() from provider { AnaliticsViewModelFactory(instance()) }

    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}