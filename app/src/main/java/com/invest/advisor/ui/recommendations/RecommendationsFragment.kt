package com.invest.advisor.ui.recommendations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.data.db.entity.EnumSecurities
import com.invest.advisor.data.db.entity.MoexEntry
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.invest.advisor.data.network.MoexNetworkDataSourceImpl
import com.invest.advisor.data.network.moexResponse.MoexApiService
import com.invest.advisor.ui.base.ScopedFragment
import com.invest.advisor.ui.moex.CONST_NOE_VALUE
import com.invest.advisor.ui.moex.MoexViewModel
import com.invest.advisor.ui.moex.MoexViewModelFactory
import kotlinx.android.synthetic.main.fragment_moex.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class RecommendationsFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private lateinit var viewModel: RecommendationsViewModel
    private val viewModelFactory: RecomendationsViewModelFactory by instance()

    private var myList: MutableList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory).get(RecommendationsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_recommendations, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindUI()

    }

    private fun bindUI() = launch {
        val marketData = viewModel.marketData.await()
        val securities = viewModel.securities.await()

        val mIssApiService = MoexApiService(ConnectivityInterceptorImpl(requireContext()))
        val moexNetworkDataSource = MoexNetworkDataSourceImpl(mIssApiService)

        moexNetworkDataSource.downloadedMarketData.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            viewModel.marketDataResponse = it

            val size = it.currentMarketData.data.size

            if (myList.isEmpty()) {
                for (i in 0 until size)
                    myList.add(it.currentMarketData.data[i][EnumMarketData.SECID.ordinal])
            }

        })


        GlobalScope.launch(Dispatchers.Main) {
            moexNetworkDataSource.fetchSecurities()
            moexNetworkDataSource.fetchMarketData()
        }
    }
}