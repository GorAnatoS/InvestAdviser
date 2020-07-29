package com.invest.advisor.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.EnumSecurities
import com.invest.advisor.data.network.response.IssApiService
import kotlinx.android.synthetic.main.fragment_securities_list.*
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
class SecuritiesListFragment : Fragment() {

    private lateinit var securitiesListViewModel: SecuritiesListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        securitiesListViewModel =
                ViewModelProviders.of(this).get(SecuritiesListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_securities_list, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        securitiesListViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        val mIssApiService = IssApiService()
//        GlobalScope.launch(Dispatchers.Main) {
//            val issApiServiceResponse = mIssApiService.getSecuritiesAndMarketDataistAsync().await()
//
//            val size = issApiServiceResponse.securities.data.size
//
//            var str: String = ""
//            for (i in 0 until size){
//                //str += "${issApiServiceResponse.securities.columns[i]} = ${issApiServiceResponse.securities.data.get(0)[i]} \n"
//                str += "${issApiServiceResponse.securities.data.get(i)[EnumSecurities.SECID.ordinal]} (${issApiServiceResponse.securities.data.get(i)[EnumSecurities.SHORTNAME.ordinal]})" +
//                        " = ${issApiServiceResponse.marketdata.data.get(i)[EnumMarketData.WAPRICE.ordinal]} , ${issApiServiceResponse.marketdata.data.get(i)[EnumMarketData.WAPTOPREVWAPRICEPRCNT.ordinal]}\n"
//            }
//
//            text_home.text = str
//        }
    }
}