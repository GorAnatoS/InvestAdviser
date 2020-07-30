package com.invest.advisor.ui.moex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.invest.advisor.data.network.MoexNetworkDataSourceImpl
import com.invest.advisor.data.network.response.MoexApiService
import com.invest.advisor.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.fragment_moex.*
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

        marketData.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            text_moex.text = it.get(0).data.get(0)[0]
        } )

        securities.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            text_moex.text = it.get(0).data.get(0)[0]
        } )
    }

}