package com.invest.advisor.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.invest.advisor.R
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioEntry
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.invest.advisor.data.network.moexResponse.MoexNetworkDataSourceImpl
import com.invest.advisor.data.network.moexResponse.MarketDataResponse
import com.invest.advisor.data.network.moexResponse.MoexApiService
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
    private lateinit var portfolioViewModel: PortfolioViewModel
    lateinit var bindingPortfolio: FragmentPortfolioBinding
    private lateinit var moexViewModel: MoexViewModel

    private val groupAdapter = GroupAdapter<GroupieViewHolder>() //TODO get rid of this parameter

    override val kodein by closestKodein()
    private val viewModelFactory: MoexViewModelFactory by instance()

    var portfolioPurchaseSum = 0.0 //total price of my portfolio when it was bought
    var currentPortfolioPrice = 0.0 //current portfolio price
    var changePrice = 0.0 //currentPortfolioPrice - portfolioPurchaseSum
    var changePercent = 0.0  // changePrice в процентах

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

        val newNumOfShares = Observer<Int> { newNum ->
            bindingPortfolio.textViewAnalize.isClickable = newNum > 0
            bindingPortfolio.textViewAnalize.isVisible = newNum > 0
        }

        portfolioViewModel.numOfShares.observe(viewLifecycleOwner, newNumOfShares)

        portfolioViewModel.allData.observe(viewLifecycleOwner, Observer {

            databaseList = it.toMutableList()

            portfolioViewModel.numOfShares.value = databaseList.size

        })

        return bindingPortfolio.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        moexViewModel = ViewModelProvider(this, viewModelFactory).get(MoexViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch {

        bindingPortfolio.textViewAdd.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.moexFragment)
        }

        bindingPortfolio.textViewAnalize.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.analiticsFragment)
        }

        val mIssApiService = MoexApiService(ConnectivityInterceptorImpl(requireContext()))
        val moexNetworkDataSource = MoexNetworkDataSourceImpl(mIssApiService)

        moexNetworkDataSource.downloadedMarketData.observe(viewLifecycleOwner, Observer { it ->
            if (it == null) return@Observer

            marketDataResponse = it

            groupAdapter.clear()

            bindingPortfolio.textViewAdd.visibility = View.VISIBLE


            calculatePortfolio()

            //expandable list setting
            val updatedList: MutableList<ExpandablePortfolioItem> = ArrayList()
            val headerList = cardItemList.toList().groupBy { it.database.secId }

            for (j in headerList.values) {

                var newItem = ExpandablePortfolioItem(
                    UserPortfolioEntry(
                        j[0].database.id,
                        j[0].database.secId,
                        j[0].database.secPrice,
                        j[0].database.secQuantity,
                        j[0].database.secPurchaseDate
                    ),
                    j[0].entryMarketData,
                    j.size > 1
                )

                for (k in j.subList(1, j.size)) {
                    newItem = ExpandablePortfolioItem(
                        UserPortfolioEntry(
                            k.database.id,
                            k.database.secId,
                            ((newItem.database.secPrice.toDouble() + k.database.secPrice.toDouble()) / 2).toString(),
                            newItem.database.secQuantity + k.database.secQuantity,
                            k.database.secPurchaseDate
                        ),
                        k.entryMarketData,
                        newItem.isExpandable
                    )
                }

                updatedList.add(newItem)
            }

            for ((index, expandableItem) in updatedList.withIndex()) {

                expandableItem.clickListener = { expandableItem ->
                    /*Toast.makeText(context, expandableItem.entryDatabase.secId, Toast.LENGTH_LONG)
                        .show()
*/
                    //Get data from detailed fragment


                    groupIndex = index
                    groupShareName = expandableItem.database.secId

                    findNavController().navigate(
                        PortfolioFragmentDirections.actionPortfolioFragmentToDetailedPortfolioItem(
                            expandableItem.database.secId
                        )
                    )
                }

                groupAdapter += ExpandableGroup(expandableItem).apply {
                    for (item in cardItemList) {
                        if (item.database.secId == expandableItem.database.secId)
                            add(
                                CardItem(
                                    item.database,
                                    item.entryMarketData
                                )
                            )
                    }
                }
            }

            bindingPortfolio.include.itemsContainer.adapter = groupAdapter


            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("key")
                ?.observe(viewLifecycleOwner) { resultDeleted ->
                    if (resultDeleted && groupIndex != -1) {

                        if (groupIndex < groupAdapter.groupCount) {
                            groupAdapter.remove(groupAdapter.getGroupAtAdapterPosition(groupIndex))

                            groupIndex = -1
                        }


                        databaseList.filter { it.secId == groupShareName }
                            .let { listOfItemsToDelete ->

                                for (item in listOfItemsToDelete) {

                                    portfolioViewModel.delete(
                                        item
                                    )

                                    databaseList.remove(item)

                                    portfolioViewModel.numOfShares.value = databaseList.size

                                    calculatePortfolio()

                                }

                            }

                        calculatePortfolio()

                        bindingPortfolio.include.itemsContainer.adapter?.notifyDataSetChanged()

                    }
                }

            bindingPortfolio.appBar.visibility = View.VISIBLE
            bindingPortfolio.include.itemsContainer.visibility = View.VISIBLE
            bindingPortfolio.include.groupLoading.visibility = View.GONE
        })

        GlobalScope.launch(Dispatchers.Main) {
            moexNetworkDataSource.fetchMarketData()
        }
    }

    private fun calculatePortfolio() {
        cardItemList = ArrayList()

        currentPortfolioPrice = 0.0
        portfolioPurchaseSum = 0.0

        for (i in databaseList)
            portfolioPurchaseSum += i.secQuantity.toDouble() * i.secPrice.toDouble()

        //first List of stocks


        for (element in marketDataResponse.currentMarketData.data)
            for (entry in databaseList.toList())
                if (entry.secId == element[EnumMarketData.SECID.ordinal]) {
                    val expandableHeaderItem = ExpandablePortfolioItem(
                        entry,
                        element,
                        false
                    )
                    cardItemList.add(expandableHeaderItem)

                    currentPortfolioPrice += entry.secQuantity.toDouble() * element[EnumMarketData.WAPRICE.ordinal].toDouble()
                }

        if (cardItemList.isNotEmpty()) {
            currentPortfolioPrice = (currentPortfolioPrice * 100).roundToInt() / 100.0

            changePrice = currentPortfolioPrice - portfolioPurchaseSum
            changePrice = (changePrice * 100).roundToInt() / 100.0

            changePercent =
                ((currentPortfolioPrice - portfolioPurchaseSum) / portfolioPurchaseSum)
            changePercent = (changePercent * 100.0).roundToInt() / 1.0
        }

        bindingPortfolio.tvPortfolioInfo.text =
            "Цена портфеля $currentPortfolioPrice₽ ${changePrice} (${changePercent}%)"

    }

    companion object {
        lateinit var marketDataResponse: MarketDataResponse

        var cardItemList: MutableList<ExpandablePortfolioItem> = ArrayList()

        lateinit var databaseList: MutableList<UserPortfolioEntry>
        private var groupIndex: Int = -1
        private var groupShareName: String = ""

    }
}