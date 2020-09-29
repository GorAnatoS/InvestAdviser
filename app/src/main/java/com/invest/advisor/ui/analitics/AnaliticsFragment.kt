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
import com.invest.advisor.data.network.response.MoexApiService
import com.invest.advisor.ui.base.ScopedFragment
import com.invest.advisor.ui.moex.MoexItem
import com.invest.advisor.ui.moex.MoexViewModel
import com.invest.advisor.ui.moex.MoexViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_moex.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AnaliticsFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: MoexViewModelFactory by instance()

    private var myList: MutableList<MoexEntry> = ArrayList()
    private var displayList: MutableList<MoexEntry> = ArrayList()
    private lateinit var viewModel: MoexViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_moex, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MoexViewModel::class.java)

        bindUI()
    }


    private fun bindUI() = launch {
        val mIssApiService = MoexApiService(ConnectivityInterceptorImpl(requireContext()))
        val moexNetworkDataSource = MoexNetworkDataSourceImpl(mIssApiService)

        moexNetworkDataSource.downloadedMarketData.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            val size = it.currentMarketData.data.size

            var receivedData: MutableList<List<String>>? = null

            var newList =
                it.currentMarketData.data.sortedBy { it -> it[EnumMarketData.WAPRICE.ordinal]?.toDouble() }

            if (myList.isEmpty()) {
                for (i in newList.indices) {
                    if (!newList[i][EnumMarketData.WAPRICE.ordinal].isNullOrEmpty())
                        myList.add(
                            MoexEntry(
                                newList[i][EnumMarketData.SECID.ordinal],
                                "",
                                if (newList[i][EnumMarketData.WAPRICE.ordinal].isNullOrEmpty()) "NoE" else newList[i][EnumMarketData.WAPRICE.ordinal],
                                if (newList[i][EnumMarketData.WAPTOPREVWAPRICE.ordinal].isNullOrEmpty()) "NoE" else newList[i][EnumMarketData.WAPTOPREVWAPRICE.ordinal],
                                if (newList[i][EnumMarketData.WAPTOPREVWAPRICEPRCNT.ordinal].isNullOrEmpty()) "NoE" else newList[i][EnumMarketData.WAPTOPREVWAPRICEPRCNT.ordinal]
                            )
                        )
                }
            }

            initRecycleView(myList.toMoexItems())

            moexNetworkDataSource.downloadedSecurities.observe(viewLifecycleOwner, Observer {
                if (it == null) return@Observer


                for (i in myList)
                    for (j in it.currentSecurities.data)
                        if (i.secId == j[EnumSecurities.SECID.ordinal]) i.secName =
                            j[EnumSecurities.SECNAME.ordinal]

                displayList.addAll(myList)


                initRecycleView(myList.toMoexItems())

                group_loading.visibility = View.GONE
            })
        })


        GlobalScope.launch(Dispatchers.Main) {
            moexNetworkDataSource.fetchSecurities()
            moexNetworkDataSource.fetchMarketData()
        }
    }

    private fun initRecycleView(items: List<MoexItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AnaliticsFragment.context)
            adapter = groupAdapter
        }
    }

    private fun MutableList<MoexEntry>.toMoexItems(): List<MoexItem> {
        return this.map {
            MoexItem(it)
        }
    }


}