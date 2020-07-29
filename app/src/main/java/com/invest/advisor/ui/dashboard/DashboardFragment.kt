package com.invest.advisor.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.invest.advisor.R
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.invest.advisor.data.network.SecuritiesNetworkDataSourceImpl
import com.invest.advisor.data.network.response.MoexApiService
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mIssApiService = MoexApiService(ConnectivityInterceptorImpl(requireContext()))
        val securitiesNetworkDataSource = SecuritiesNetworkDataSourceImpl(mIssApiService)

        securitiesNetworkDataSource.downloadedSecurities.observe(viewLifecycleOwner, Observer {

            //val issApiServiceResponse = mIssApiService.getSecuritiesSECIDAsync().await()

            val size = it.currentSecurities.columns.size

            var str: String = ""
            for (i in 0 until size){
                str += "${it.currentSecurities.data.get(i)[0]}\n"
            }

            text_dashboard.text = str
        })

        GlobalScope.launch(Dispatchers.Main) {
            //val temp = mIssApiService.getSecuritiesListAsync().await()
            //text_dashboard.text =  temp.currentSecurities.data.get(0)[0]

            securitiesNetworkDataSource.fetchSecurities()
        }
    }
}