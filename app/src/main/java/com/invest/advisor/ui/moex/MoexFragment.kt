package com.invest.advisor.ui.moex

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.data.db.entity.EnumSecurities
import com.invest.advisor.data.db.entity.MoexEntry
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.invest.advisor.data.network.MoexNetworkDataSourceImpl
import com.invest.advisor.data.network.response.MoexApiService
import com.invest.advisor.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_moex.*
import kotlinx.android.synthetic.main.item_moex.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

/**
 * Класс для вывода общего списка данных MOEX за день с основными значениями:
 * secid
 * shortname
 * prevprice
 * warchange
 */
class MoexFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: MoexViewModelFactory by instance()

    private var myList: MutableList<MoexEntry> = ArrayList()
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
        val marketData = viewModel.marketData.await()
        val securities = viewModel.securities.await()

        val mIssApiService = MoexApiService(ConnectivityInterceptorImpl(requireContext()))
        val moexNetworkDataSource = MoexNetworkDataSourceImpl(mIssApiService)

        moexNetworkDataSource.downloadedMarketData.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            val size = it.currentMarketData.data.size

            for (i in 0 until size) {
                myList.add(
                    MoexEntry(
                        it.currentMarketData.data[i][EnumMarketData.SECID.ordinal],
                        "",
                        if (it.currentMarketData.data[i][EnumMarketData.WAPRICE.ordinal].isNullOrEmpty()) "0" else it.currentMarketData.data[i][EnumMarketData.WAPRICE.ordinal],
                        if (it.currentMarketData.data[i][EnumMarketData.CHANGE.ordinal].isNullOrEmpty()) "0" else it.currentMarketData.data[i][EnumMarketData.CHANGE.ordinal]
                    )
                )
            }
            initRecycleView(myList.toMoexItems())

            moexNetworkDataSource.downloadedSecurities.observe(viewLifecycleOwner, Observer {
                if (it == null) return@Observer

                val size = it.currentSecurities.data.size

                for (i in 0 until myList.size)
                    myList[i].secName = it.currentSecurities.data[i][EnumSecurities.SECNAME.ordinal]

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
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MoexFragment.context)
            adapter = groupAdapter
        }
    }

    private fun MutableList<MoexEntry>.toMoexItems(): List<MoexItem> {
        return this.map {
            MoexItem(it)
        }
    }
}