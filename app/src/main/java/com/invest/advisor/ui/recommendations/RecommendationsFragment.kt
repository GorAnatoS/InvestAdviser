package com.invest.advisor.ui.recommendations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.invest.advisor.data.network.MoexNetworkDataSourceImpl
import com.invest.advisor.data.network.YahooNetworkDataSourceImpl
import com.invest.advisor.data.network.moexResponse.MoexApiService
import com.invest.advisor.data.network.yahooResponse.YahooApiService
import com.invest.advisor.data.network.yahooResponse.YahooResponse
import com.invest.advisor.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.fragment_recommendations.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.ArrayList

class RecommendationsFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: RecommendationsViewModelFactory by instance()

    private lateinit var viewModel: RecommendationsViewModel

    private var myList: MutableList<String> = ArrayList()

    lateinit var mYahooNetworkDataSource: YahooNetworkDataSourceImpl
    lateinit var mYahooApiService: YahooApiService

    var arrayList: ArrayList<YahooResponse> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory).get(RecommendationsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_recommendations, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindUI()



      /*  arrayList.sortBy { it.quoteSummary.result[0].assetProfile.sector }
        text_recommendations.text = arrayList.toString()*/
    }

    private fun bindUI() = launch {
        val marketData = viewModel.marketData.await()
        val securities = viewModel.securities.await()

        val mIssApiService = MoexApiService(ConnectivityInterceptorImpl(requireContext()))
        val moexNetworkDataSource = MoexNetworkDataSourceImpl(mIssApiService)

        mYahooApiService = YahooApiService(ConnectivityInterceptorImpl(requireContext()))
        mYahooNetworkDataSource = YahooNetworkDataSourceImpl(mYahooApiService)

        moexNetworkDataSource.downloadedMarketData.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            viewModel.marketDataResponse = it

            val size = it.currentMarketData.data.size

            if (myList.isEmpty()) {
                for (i in 0 until size)//12)// size)
                    myList.add(it.currentMarketData.data[i][EnumMarketData.SECID.ordinal])
            }

            GlobalScope.launch(Dispatchers.Main) {

                val requests = ArrayList<Any>()

                for (element in myList) {
                    requests.add(mYahooNetworkDataSource.fetchYahooData(element + ".ME"))
                }

                text_recommendations.text = "XXXXXXXXXX\n"
                arrayList.sortBy { it.quoteSummary.result[0].price.regularMarketPrice.raw }
                val result = arrayList.groupBy { it.quoteSummary.result[0].assetProfile?.sector }


                for (e in result)
                     text_recommendations.text = text_recommendations.text.toString() + e.key + " " + e.value.size + "\n"
                /*for ((index, e) in arrayList.withIndex())
                    text_recommendations.text = text_recommendations.text.toString() + arrayList[index].quoteSummary.result[0].price.shortName + " " + arrayList[index].quoteSummary.result[0].price.regularMarketPrice.fmt+ "\n"*/


            }

            text_recommendations.text = ""


        })



        mYahooNetworkDataSource.downloadedYahooResponse.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            arrayList.add(it)

           /* text_recommendations.text =
                text_recommendations.text.toString() + it.quoteSummary.result[0].price.shortName + " " + it.quoteSummary.result[0].assetProfile?.sector + " " + it.quoteSummary.result[0].assetProfile?.industry + "\n"*/

        })

        GlobalScope.launch(Dispatchers.Main) {
            moexNetworkDataSource.fetchSecurities()
            moexNetworkDataSource.fetchMarketData()
        }
    }
}