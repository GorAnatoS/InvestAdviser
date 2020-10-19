package com.invest.advisor.ui.analitics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.invest.advisor.R
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.invest.advisor.data.network.YahooNetworkDataSourceImpl
import com.invest.advisor.data.network.yahooResponse.YahooApiService
import com.invest.advisor.ui.base.ScopedFragment
import com.invest.advisor.ui.portfolio.PortfolioViewModel
import kotlinx.android.synthetic.main.fragment_analitics.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class AnaliticsFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    lateinit var mYahooNetworkDataSource: YahooNetworkDataSourceImpl

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        portfolioViewModel = ViewModelProvider(this).get(PortfolioViewModel::class.java)

        val mYahooApiService = YahooApiService(ConnectivityInterceptorImpl(requireContext()))
        mYahooNetworkDataSource = YahooNetworkDataSourceImpl(mYahooApiService)

        return inflater.inflate(R.layout.fragment_analitics, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindUI()
    }

    private fun bindUI() = launch {

        mYahooNetworkDataSource.downloadedYahooResponse.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            text_analitics.text =
                text_analitics.text.toString() + it.quoteSummary.result[0].price.shortName + " " + it.quoteSummary.result[0].assetProfile.industry + "  " + it.quoteSummary.result[0].assetProfile.sector + " " + it.quoteSummary.result[0].financialData.currentPrice.raw + '\n'
        })

        portfolioViewModel.allData.observe(viewLifecycleOwner, Observer {
            GlobalScope.launch(Dispatchers.IO) {
                for (i in it) {
                    mYahooNetworkDataSource.fetchYahooData(i.secId + ".ME")
                }
            }
        })
    }

    companion object {
        private lateinit var portfolioViewModel: PortfolioViewModel
    }
}