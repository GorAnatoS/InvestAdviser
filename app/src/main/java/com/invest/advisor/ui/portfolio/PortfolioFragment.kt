package com.invest.advisor.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.data.db.userPortfolio.UserPortfolioEntry
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.invest.advisor.data.network.MoexNetworkDataSourceImpl
import com.invest.advisor.data.network.response.MoexApiService
import com.invest.advisor.databinding.FragmentPortfolioBinding
import com.invest.advisor.ui.base.ScopedFragment
import com.invest.advisor.ui.moex.MoexViewModel
import com.invest.advisor.ui.moex.MoexViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class PortfolioFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: MoexViewModelFactory by instance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingPortfolio =
            DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio, container, false)

        portfolioViewModel =
            ViewModelProvider(this).get(PortfolioViewModel::class.java)

        bindingPortfolio.userPortfolioViewModel = portfolioViewModel
        bindingPortfolio.lifecycleOwner = this

        portfolioViewModel.allData.observe(viewLifecycleOwner, Observer {
            bindingPortfolio.itemsContainer.adapter =
                GroupAdapter<GroupieViewHolder>().apply {
                    addAll(it.toPortfolioItemCards().sortedBy { it.itemContent.secId })
                }
        })

        return bindingPortfolio.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        moexViewModel = ViewModelProvider(this, viewModelFactory).get(MoexViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch {
        val marketData = moexViewModel.marketData.await()
        val securities = moexViewModel.securities.await()

        val mIssApiService = MoexApiService(ConnectivityInterceptorImpl(requireContext()))
        val moexNetworkDataSource = MoexNetworkDataSourceImpl(mIssApiService)

        moexNetworkDataSource.downloadedMarketData.observe(viewLifecycleOwner, Observer { it ->
            if (it == null) return@Observer

            var updatedList: MutableList<PortfolioItemCard> = ArrayList()

            for (element in it.currentMarketData.data)
                for (entry in portfolioViewModel.allData.value?.toList()!!)
                    if (entry.secId == element[EnumMarketData.SECID.ordinal]) {
                        var currentPrice =
                            (entry.secQuantity.toDouble() * element[EnumMarketData.WAPRICE.ordinal].toDouble())
                        currentPrice = Math.round(currentPrice * 100.0) / 100.0

                        var oldPrice = entry.secPrice.toDouble() * entry.secQuantity.toDouble()

                        var changePcnt = (element[EnumMarketData.WAPRICE.ordinal].toDouble() - entry.secPrice.toDouble()) / entry.secPrice.toDouble() * 100// entry.secQuantity.toDouble()
                        changePcnt = Math.round(changePcnt * 100.0) / 100.0

                        var changePrice = currentPrice - oldPrice
                        changePrice = Math.round(changePrice * 100.0) / 100.0


                        updatedList.add(
                            PortfolioItemCard(
                                PortfolioItemContent(
                                    entry.secId,
                                    element[EnumMarketData.WAPRICE.ordinal],
                                    entry.secQuantity,
                                    currentPrice.toString(),
                                    changePcnt.toString(),
                                    changePrice
                                )
                            )
                        )
                    }


            bindingPortfolio.itemsContainer.adapter =
                GroupAdapter<GroupieViewHolder>().apply {
                    clear()
                    addAll(updatedList)
                }

        })


        GlobalScope.launch(Dispatchers.Main) {
            moexNetworkDataSource.fetchMarketData()
        }

    }

    private fun List<UserPortfolioEntry>.toPortfolioItemCards(): List<PortfolioItemCard> {
        return this.map {
            PortfolioItemCard(
                PortfolioItemContent(
                    it.secId,
                    it.secPrice,
                    it.secQuantity
                )
            )
        }
    }

    companion object {
        private lateinit var portfolioViewModel: PortfolioViewModel
        lateinit var bindingPortfolio: FragmentPortfolioBinding
        private lateinit var moexViewModel: MoexViewModel
    }
}