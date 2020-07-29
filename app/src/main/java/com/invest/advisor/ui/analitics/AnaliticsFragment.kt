package com.invest.advisor.ui.portfolio

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
import com.invest.advisor.data.network.MoexNetworkDataSourceImpl
import com.invest.advisor.data.network.response.MoexApiService
import kotlinx.android.synthetic.main.fragment_portfolio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AnaliticsFragment : Fragment() {

    private lateinit var analiticsViewModel: AnaliticsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        analiticsViewModel =
                ViewModelProviders.of(this).get(AnaliticsViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_analitics, container, false)


        return root
    }


}