package com.invest.advisor.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
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
import com.invest.advisor.ui.portfolio.portfolioItems.CardItem
import com.invest.advisor.ui.portfolio.portfolioItems.ExpandablePortfolioItem
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.groupiex.plusAssign
import kotlinx.android.synthetic.main.content_fragment_portfolio.view.*
import kotlinx.android.synthetic.main.fragment_portfolio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import kotlin.math.roundToInt

const val INSET_TYPE_KEY = "inset_type"
const val INSET = "inset"

class PortfolioFragment : ScopedFragment(), KodeinAware {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>() //TODO get rid of this parameter

    override val kodein by closestKodein()
    private val viewModelFactory: MoexViewModelFactory by instance()

    var portfolioPurchaseSum = 0.0
    var currentPortfolioPrice = 0.0

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

            for (i in it)
                portfolioPurchaseSum += i.secQuantity.toDouble() * i.secPrice.toDouble()

        })

        return bindingPortfolio.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        moexViewModel = ViewModelProvider(this, viewModelFactory).get(MoexViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch {

        textView_add.setOnClickListener{
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.moexFragment)
        }

        textView_analize.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.analiticsFragment)
        }

        val mIssApiService = MoexApiService(ConnectivityInterceptorImpl(requireContext()))
        val moexNetworkDataSource = MoexNetworkDataSourceImpl(mIssApiService)

        moexNetworkDataSource.downloadedMarketData.observe(viewLifecycleOwner, Observer { it ->
            if (it == null) return@Observer

            textView_analize.visibility = View.VISIBLE
            textView_add.visibility = View.VISIBLE
            
            //first List of stocks
            val cardItemList: MutableList<ExpandablePortfolioItem> = ArrayList()

            for (element in it.currentMarketData.data)
                for (entry in portfolioViewModel.allData.value?.toList()!!)
                    if (entry.secId == element[EnumMarketData.SECID.ordinal]) {
                        val expandableHeaderItem = ExpandablePortfolioItem(
                            entry,
                            element,
                            false
                        )
                        cardItemList.add(expandableHeaderItem)

                        currentPortfolioPrice += entry.secQuantity.toDouble() * element[EnumMarketData.WAPRICE.ordinal].toDouble()
                    }

            //update my portfolio
            // TODO: 9/21/2020 null and 0 / check
            currentPortfolioPrice = (currentPortfolioPrice * 100).roundToInt() / 100.0

            var changePrice = currentPortfolioPrice - portfolioPurchaseSum
            changePrice = (changePrice * 100).roundToInt() / 100.0

            var changePercent =
                ((currentPortfolioPrice - portfolioPurchaseSum) / portfolioPurchaseSum)
            changePercent = (changePercent * 100.0).roundToInt() / 1.0

            bindingPortfolio.tvPortfolioInfo.text =
                "Цена портфеля $currentPortfolioPrice₽ ${changePrice} (${changePercent}%)"

            val updatedList: MutableList<ExpandablePortfolioItem> = ArrayList()

            val headerList = cardItemList.toList().groupBy { it.entryDatabase.secId }

            for (j in headerList.values) {

                var newItem = ExpandablePortfolioItem(
                    UserPortfolioEntry(
                        j[0].entryDatabase.id,
                        j[0].entryDatabase.secId,
                        j[0].entryDatabase.secPrice,
                        j[0].entryDatabase.secQuantity,
                        j[0].entryDatabase.secPurchaseDate
                    ),
                    j[0].entryMarketData,
                    j.size > 1
                )

                for (k in j.subList(1, j.size)) {
                    newItem = ExpandablePortfolioItem(
                        UserPortfolioEntry(
                            k.entryDatabase.id,
                            k.entryDatabase.secId,
                            ((newItem.entryDatabase.secPrice.toDouble() + k.entryDatabase.secPrice.toDouble()) / 2).toString(),
                            newItem.entryDatabase.secQuantity + k.entryDatabase.secQuantity,
                            k.entryDatabase.secPurchaseDate
                        ),
                        k.entryMarketData,
                        newItem.isExpandable
                    )
                }

                updatedList.add(newItem)
            }

            for (expandableItem in updatedList) {
                groupAdapter += ExpandableGroup(expandableItem).apply {
                    for (item in cardItemList) {
                        if (item.entryDatabase.secId == expandableItem.entryDatabase.secId)
                            add(
                                CardItem(
                                    item.entryDatabase,
                                    item.entryMarketData
                                )
                            )
                    }
                }
            }

            bindingPortfolio.include.items_container.adapter = groupAdapter
        })

        GlobalScope.launch(Dispatchers.Main) {
            moexNetworkDataSource.fetchMarketData()
        }
    }

    companion object {
        private lateinit var portfolioViewModel: PortfolioViewModel
        lateinit var bindingPortfolio: FragmentPortfolioBinding
        private lateinit var moexViewModel: MoexViewModel
    }
}

// TODO: 9/26/2020 Время отображаеть без :