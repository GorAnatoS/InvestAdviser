package com.invest.advisor.ui.analitics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.data.db.entity.EnumSecurities
import com.invest.advisor.data.db.entity.MoexEntry
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.invest.advisor.data.network.MoexNetworkDataSourceImpl
import com.invest.advisor.data.network.YahooNetworkDataSourceImpl
import com.invest.advisor.data.network.moexResponse.MoexApiService
import com.invest.advisor.data.network.yahooResponse.YahooApiService
import com.invest.advisor.ui.base.ScopedFragment
import com.invest.advisor.ui.moex.MoexItem
import com.invest.advisor.ui.moex.MoexViewModel
import com.invest.advisor.ui.moex.MoexViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_analitics.*
import kotlinx.android.synthetic.main.fragment_moex.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AnaliticsFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: AnaliticsViewModelFactory by instance()

    private lateinit var viewModel: AnaliticsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analitics, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindUI()
    }


    private fun bindUI() = launch {
        val mYahooApiService = YahooApiService(ConnectivityInterceptorImpl(requireContext()))
        val mYahooNetworkDataSource = YahooNetworkDataSourceImpl(mYahooApiService)

        mYahooNetworkDataSource.downloadedYahooResponse.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            text_analitics.text = it.quoteSummary.result[0].assetProfile.industry + '\n' + it.quoteSummary.result[0].financialData.currentPrice.raw
        })

        GlobalScope.launch(Dispatchers.Main) {
            mYahooNetworkDataSource.fetchYahooData("YNDX")
        }
    }


}