package com.invest.advisor.ui.moexsecurities

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.data.db.entity.EnumSecurities
import com.invest.advisor.data.db.entity.MoexSecurityEntry
import com.invest.advisor.data.network.ConnectivityInterceptor
import com.invest.advisor.data.network.moexResponse.MoexNetworkDataSourceImpl
import com.invest.advisor.data.network.moexResponse.MoexApiService
import com.invest.advisor.databinding.FragmentMoexBinding
import com.invest.advisor.ui.base.ScopedFragment
import com.invest.advisor.ui.moexsecurities.items.MoexSecurityItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.ArrayList


/**
 * MoexSecuritiesListFragment with necessary MOEX data like:
 * secid
 * shortname
 * prevprice
 * warchange
 */

class MoexSecuritiesListFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: MoexViewModelFactory by instance()

    lateinit var binding: FragmentMoexBinding

    private var moexSecurityDataList: MutableList<MoexSecurityEntry> = ArrayList()
    private var displayedMoexSecurityDataList: MutableList<MoexSecurityEntry> = ArrayList()
    private lateinit var moexSecuritiesListViewModel: MoexSecuritiesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_moex,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        moexSecuritiesListViewModel = ViewModelProvider(this, viewModelFactory).get(MoexSecuritiesListViewModel::class.java)

        bindUI()

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.moex_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val sortItem = menu.findItem(R.id.action_sort)

        sortItem.setOnMenuItemClickListener {

            showDialog()
            true

        }

        val searchView = searchItem?.actionView as SearchView
        val theTextArea = searchView.findViewById<View>(R.id.search_src_text) as SearchAutoComplete
        theTextArea.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorSearchTextHint))
        theTextArea.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.colorSearchTextHint))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText!!.isNotEmpty()) {

                    displayedMoexSecurityDataList.clear()
                    val search = newText.lowercase(Locale.getDefault())

                    moexSecurityDataList.forEach {
                        if (it.secId!!.lowercase(Locale.getDefault()).contains(newText) || it.secName!!.lowercase(Locale.getDefault())
                                .contains(newText)
                        )
                            displayedMoexSecurityDataList.add(it)
                    }

                    initRecycleView(displayedMoexSecurityDataList.toMoexItems())
                } else {
                    displayedMoexSecurityDataList.clear()
                    displayedMoexSecurityDataList.addAll(moexSecurityDataList)
                }

                return true
            }

        })

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun bindUI() = launch {
        val marketData = moexSecuritiesListViewModel.marketData.await()
        val securities = moexSecuritiesListViewModel.securities.await()

        val mIssApiService = MoexApiService(ConnectivityInterceptor(requireContext()))
        val moexNetworkDataSource = MoexNetworkDataSourceImpl(mIssApiService)

        moexNetworkDataSource.downloadedMarketData.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            moexSecuritiesListViewModel.marketDataResponse = it

            val size = it.currentMarketData.data.size

            if (moexSecurityDataList.isEmpty()) {
                for (i in 0 until size) {
                    //if (!it.currentMarketData.data[i][EnumMarketData.WAPRICE.ordinal].isNullOrEmpty())
                    moexSecurityDataList.add(
                        MoexSecurityEntry(
                            it.currentMarketData.data[i][EnumMarketData.SECID.ordinal],
                            "",
                            if (it.currentMarketData.data[i][EnumMarketData.WAPRICE.ordinal].isNullOrEmpty()) CONST_NOE_VALUE else it.currentMarketData.data[i][EnumMarketData.WAPRICE.ordinal],
                            if (it.currentMarketData.data[i][EnumMarketData.WAPTOPREVWAPRICE.ordinal].isNullOrEmpty()) CONST_NOE_VALUE else it.currentMarketData.data[i][EnumMarketData.WAPTOPREVWAPRICE.ordinal],
                            if (it.currentMarketData.data[i][EnumMarketData.WAPTOPREVWAPRICEPRCNT.ordinal].isNullOrEmpty()) CONST_NOE_VALUE else it.currentMarketData.data[i][EnumMarketData.WAPTOPREVWAPRICEPRCNT.ordinal]
                        )
                    )
                }
            }

            initRecycleView(moexSecurityDataList.toMoexItems())

            moexNetworkDataSource.downloadedSecurities.observe(viewLifecycleOwner, Observer {
                if (it == null) return@Observer

                moexSecuritiesListViewModel.securitiesResponse = it

                for (i in 0 until moexSecurityDataList.size) {
                    moexSecurityDataList[i].secName = it.currentSecurities.data[i][EnumSecurities.SECNAME.ordinal]
                }

                displayedMoexSecurityDataList.addAll(moexSecurityDataList)

                initRecycleView(moexSecurityDataList.toMoexItems())

                binding.groupLoading.visibility = View.GONE
            })
        })


        GlobalScope.launch(Dispatchers.Main) {
            moexNetworkDataSource.fetchSecurities()
            moexNetworkDataSource.fetchMarketData()
        }
    }

    private fun initRecycleView(securityItems: List<MoexSecurityItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(securityItems.filter { it.moexSecurityEntry.secPrice != CONST_NOE_VALUE })
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MoexSecuritiesListFragment.context)
            adapter = groupAdapter
        }
    }

    private fun MutableList<MoexSecurityEntry>.toMoexItems(): List<MoexSecurityItem> {
        return this.map {
            MoexSecurityItem(it)
        }
    }


    var array = arrayOf("По названию ↑", "По цене ↑", "По изменению за день ↑")

    // Method to show an alert dialog with multiple choice list items
    private fun showDialog() {
        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(requireContext())


        //val array  = arrayOf("По названию ↑", "По цене ↑", "По изменению за день ↑").toMutableList()

        // Set a title for alert dialog
        builder.setTitle("Сортировать список")
            .setItems(array, DialogInterface.OnClickListener { dialog, which ->
                // The 'which' argument contains the index position
                // of the selected item

                val newList: List<List<String>>

                when (which) {
                    EnumSortOptions.BY_WARPRICE.sortTypeOrder -> {
                        displayedMoexSecurityDataList.clear()
                        if (array[which][array[which].length - 1] == '↑') {
                            newList =
                                moexSecuritiesListViewModel.marketDataResponse.currentMarketData.data.sortedBy { it[EnumMarketData.WAPRICE.ordinal].toDouble() }

                            array[which] = array[which].replace('↑', '↓')
                        } else {
                            newList =
                                moexSecuritiesListViewModel.marketDataResponse.currentMarketData.data.sortedByDescending { it[EnumMarketData.WAPRICE.ordinal].toDouble() }
                            array[which] = array[which].replace('↓', '↑')
                        }

                        addElementsToDisplayList(newList)
                        initRecycleView(displayedMoexSecurityDataList.toMoexItems())
                    }

                    EnumSortOptions.BY_DAY_CHANGE.sortTypeOrder -> {
                        displayedMoexSecurityDataList.clear()


                        if (array[which][array[which].length - 1] == '↑') {
                            newList =
                                moexSecuritiesListViewModel.marketDataResponse.currentMarketData.data.sortedBy { it[EnumMarketData.WAPTOPREVWAPRICEPRCNT.ordinal].toDouble() }

                            array[which] = array[which].replace('↑', '↓')
                        } else {
                            newList =
                                moexSecuritiesListViewModel.marketDataResponse.currentMarketData.data.sortedByDescending { it[EnumMarketData.WAPTOPREVWAPRICEPRCNT.ordinal].toDouble() }
                            array[which] = array[which].replace('↓', '↑')
                        }

                        addElementsToDisplayList(newList)
                        initRecycleView(displayedMoexSecurityDataList.toMoexItems())
                    }

                    EnumSortOptions.BY_NAME.sortTypeOrder -> {
                        displayedMoexSecurityDataList.clear()

                        if (array[which][array[which].length - 1] == '↑') {
                            newList =
                                moexSecuritiesListViewModel.marketDataResponse.currentMarketData.data.sortedBy { it[EnumMarketData.SECID.ordinal] }

                            array[which] = array[which].replace('↑', '↓')
                        } else {
                            newList =
                                moexSecuritiesListViewModel.marketDataResponse.currentMarketData.data.sortedByDescending { it[EnumMarketData.SECID.ordinal] }
                            array[which] = array[which].replace('↓', '↑')
                        }

                        addElementsToDisplayList(newList)
                        initRecycleView(displayedMoexSecurityDataList.toMoexItems())
                    }
                }
            })

        dialog = builder.create()

        dialog.show()
    }

    private fun addElementsToDisplayList(list: List<List<String>>) {

        val secList = moexSecuritiesListViewModel.securitiesResponse.currentSecurities.data

        for (i in list.indices) {
            if (!list[i][EnumMarketData.WAPRICE.ordinal].isNullOrEmpty())
                displayedMoexSecurityDataList.add(
                    MoexSecurityEntry(
                        list[i][EnumMarketData.SECID.ordinal],
                        secList.find { it[0] == list[i][EnumMarketData.SECID.ordinal] }
                            ?.get(EnumSecurities.SECNAME.ordinal) ?: "",
                        if (list[i][EnumMarketData.WAPRICE.ordinal].isNullOrEmpty()) CONST_NOE_VALUE else list[i][EnumMarketData.WAPRICE.ordinal],
                        if (list[i][EnumMarketData.WAPTOPREVWAPRICE.ordinal].isNullOrEmpty()) CONST_NOE_VALUE else list[i][EnumMarketData.WAPTOPREVWAPRICE.ordinal],
                        if (list[i][EnumMarketData.WAPTOPREVWAPRICEPRCNT.ordinal].isNullOrEmpty()) CONST_NOE_VALUE else list[i][EnumMarketData.WAPTOPREVWAPRICEPRCNT.ordinal]
                    )
                )
        }

    }

}

const val CONST_NOE_VALUE = "NoE" //Null or Empty value

enum class EnumSortOptions(val sortTypeOrder: Int) {
    BY_NAME(0),
    BY_WARPRICE(1),
    BY_DAY_CHANGE(2),
}