package com.invest.advisor.ui.moex

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
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
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_moex.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.ArrayList

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

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.moex_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText!!.isNotEmpty()) {

                    displayList.clear()
                    val search = newText.toLowerCase(Locale.getDefault())

                    myList.forEach {
                        if (it.secId!!.toLowerCase().contains(newText) || it.secName!!.toLowerCase().contains(newText))
                            displayList.add(it)
                    }

                    initRecycleView(displayList.toMoexItems())
                } else {
                    displayList.clear()
                    displayList.addAll(myList)
                    initRecycleView(displayList.toMoexItems())
                }

                return true
            }

        })

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun bindUI() = launch {
        val marketData = viewModel.marketData.await()
        val securities = viewModel.securities.await()

        val mIssApiService = MoexApiService(ConnectivityInterceptorImpl(requireContext()))
        val moexNetworkDataSource = MoexNetworkDataSourceImpl(mIssApiService)

        moexNetworkDataSource.downloadedMarketData.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            val size = it.currentMarketData.data.size

            if (myList.isEmpty()) {
                for (i in 0 until size) {

                    myList.add(
                        MoexEntry(
                            it.currentMarketData.data[i][EnumMarketData.SECID.ordinal],
                            "",
                            if (it.currentMarketData.data[i][EnumMarketData.WAPRICE.ordinal].isNullOrEmpty()) "NoE" else it.currentMarketData.data[i][EnumMarketData.WAPRICE.ordinal],
                            if (it.currentMarketData.data[i][EnumMarketData.WAPTOPREVWAPRICE.ordinal].isNullOrEmpty()) "NoE" else it.currentMarketData.data[i][EnumMarketData.WAPTOPREVWAPRICE.ordinal],
                            if (it.currentMarketData.data[i][EnumMarketData.WAPTOPREVWAPRICEPRCNT.ordinal].isNullOrEmpty()) "NoE" else it.currentMarketData.data[i][EnumMarketData.WAPTOPREVWAPRICEPRCNT.ordinal]
                        )
                    )
                }
            }

            initRecycleView(myList.toMoexItems())

            moexNetworkDataSource.downloadedSecurities.observe(viewLifecycleOwner, Observer {
                if (it == null) return@Observer

                for (i in 0 until myList.size)
                    myList[i].secName = it.currentSecurities.data[i][EnumSecurities.SECNAME.ordinal]

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

// TODO: 9/13/2020 add search option  https://demonuts.com/android-recyclerview-search/#edit https://howtodoandroid.com/search-filter-recyclerview-android/

//TODO 2020/09/13 22:27 || что надо для первого релиза?
// TODO: 9/13/2020 сделать поиск 
// TODO: 9/13/2020 сделать в моем портфеле сброс и удаление 
// TODO: 9/13/2020 сделать в портфеле сворачивание одной акции и удаление ее
//https://developer.android.com/guide/topics/search/search-dialog
// TODO: 9/13/2020 сделать аналитику?