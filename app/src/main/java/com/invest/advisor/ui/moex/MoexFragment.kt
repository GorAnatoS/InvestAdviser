package com.invest.advisor.ui.moex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.invest.advisor.data.network.MoexNetworkDataSourceImpl
import com.invest.advisor.data.network.response.MoexApiService
import kotlinx.android.synthetic.main.fragment_moex.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Класс для вывода общего списка данных MOEX за день с основными значениями:
 * secid
 * shortname
 * prevprice
 * warchange
 */
class MoexFragment : Fragment() {

    private lateinit var moexViewModel: MoexViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        moexViewModel =
                ViewModelProviders.of(this).get(MoexViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_moex, container, false)
        val textView: TextView = root.findViewById(R.id.text_analitics)
        moexViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mIssApiService = MoexApiService(ConnectivityInterceptorImpl(requireContext()))
        val marketDataNetworkDataSource = MoexNetworkDataSourceImpl(mIssApiService)

        marketDataNetworkDataSource.downloadedMarketData.observe(viewLifecycleOwner, Observer {

            //val issApiServiceResponse = mIssApiService.getSecuritiesSECIDAsync().await()

            val size = it.currentMarketData.data.size

            var str: String = ""
            for (i in 0 until size){
                str += "${it.currentMarketData.data.get(i)[0]} ${it.currentMarketData.data.get(i)[EnumMarketData.WAPRICE.ordinal]}\n"
            }

            text_analitics.text = str
        })

        GlobalScope.launch(Dispatchers.Main) {
            //val temp = mIssApiService.getSecuritiesListAsync().await()
            //text_dashboard.text =  temp.currentSecurities.data.get(0)[0]

            marketDataNetworkDataSource.fetchMarketData()
        }
    }
}