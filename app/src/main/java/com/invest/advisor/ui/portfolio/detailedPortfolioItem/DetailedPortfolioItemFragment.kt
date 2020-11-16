package com.invest.advisor.ui.portfolio.detailedPortfolioItem

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import com.invest.advisor.R
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.invest.advisor.data.network.YahooNetworkDataSourceImpl
import com.invest.advisor.data.network.yahooResponse.YahooApiService
import com.invest.advisor.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.detailed_portfolio_item_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class DetailedPortfolioItemFragment : ScopedFragment(), KodeinAware {

   // var resultDeleted: Boolean = false
    override val kodein by closestKodein()

    companion object {
        lateinit var mYahooNetworkDataSource: YahooNetworkDataSourceImpl
        lateinit var mYahooApiService: YahooApiService

        //private lateinit var portfolioViewModel: PortfolioViewModel

        fun newInstance() = DetailedPortfolioItemFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.detailed_portfolio_item_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detailed_portfolio_item_menu, menu)

        val deleteItemFromDB = menu?.findItem(R.id.delete_item_from_db)

        deleteItemFromDB.setOnMenuItemClickListener {
            deleteItemFromDB()
            true
        }

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun deleteItemFromDB() {

        //Toast.makeText(context, "УДалить", Toast.LENGTH_SHORT).show()
        //todo согласны на удаление?
        //da - удалять

        var resultDeleted = true

        findNavController().previousBackStackEntry?.savedStateHandle?.set("key", resultDeleted)
        //findNavController().previousBackStackEntry?.savedStateHandle?.set("name", arguments?.getString("itemNumberInDB"))

        Toast.makeText(context, getString(R.string.deleted), Toast.LENGTH_SHORT).show()


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mYahooApiService = YahooApiService(ConnectivityInterceptorImpl(requireContext()))
        mYahooNetworkDataSource = YahooNetworkDataSourceImpl(mYahooApiService)

        GlobalScope.launch {
            mYahooNetworkDataSource.fetchYahooData(arguments?.getString("itemNumberInDB") + ".ME")
        }

        mYahooNetworkDataSource.downloadedYahooResponse.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            tvName.text = it.quoteSummary.result[0].price.shortName

            tvCurrentPrice.text =
                it.quoteSummary.result[0].price.regularMarketPrice.raw.toString() + " (" +
                        it.quoteSummary.result[0].price.regularMarketChange.raw.toString() + ", " +
                        it.quoteSummary.result[0].price.regularMarketChangePercent.raw.toString() + "%)"

            sliderDayPriceRange.apply {
                valueFrom = it.quoteSummary.result[0].price.regularMarketDayLow.raw.toFloat()
                valueTo = it.quoteSummary.result[0].price.regularMarketDayHigh.raw.toFloat()
                value = it.quoteSummary.result[0].price.regularMarketPrice.raw.toFloat()



                tvRegularMarketDayHigh.text =
                    it.quoteSummary.result[0].price.regularMarketDayHigh.raw.toString()
                tvRegularMarketDayLow.text =
                    it.quoteSummary.result[0].price.regularMarketDayLow.raw.toString()
            }

            sliderDayPriceRange.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: Slider) {
                    //println("Start Tracking Touch")
                }

                override fun onStopTrackingTouch(slider: Slider) {
                    sliderDayPriceRange.value =
                        it.quoteSummary.result[0].price.regularMarketPrice.raw.toFloat()
                }
            })

            // TODO: 11/16/2020 изменить бэкграунд при нажджатии на слайдер


            //.apply {
            //text =


/*
                text = text.toString() + it.quoteSummary.result[0].price.exchange + "\n"
                text = text.toString() + it.quoteSummary.result[0].price.exchangeName + "\n"
                text = text.toString() + it.quoteSummary.result[0].price.fromCurrency.toString() + "\n"
                text = text.toString() + it.quoteSummary.result[0].price.marketCap + "\n"
                text = text.toString() + it.quoteSummary.result[0].price.marketState + "\n"

                text = text.toString() + it.quoteSummary.result[0].price.postMarketChange + "\n"
                text = text.toString() + it.quoteSummary.result[0].price.postMarketPrice + "\n"
                text = text.toString() + it.quoteSummary.result[0].price.preMarketChange + "\n"
                text = text.toString() + it.quoteSummary.result[0].price.preMarketPrice + "\n"
                text = text.toString() + it.quoteSummary.result[0].price.priceHint + "\n"
                text = text.toString() + it.quoteSummary.result[0].price.strikePrice + "\n"
                text = text.toString() + it.quoteSummary.result[0].price.volume24Hr + "\n"
                text = text.toString() + it.quoteSummary.result[0].price.volumeAllCurrencies + "\n\n\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.targetMeanPrice + "\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.debtToEquity + "\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.currentRatio + "\n"*/

            /*      text = text.toString() + it.quoteSummary.result[0].financialData.earningsGrowth + "\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.ebitda + "\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.ebitdaMargins + "\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.targetMeanPrice + "\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.freeCashflow + "\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.profitMargins + "\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.quickRatio + "\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.recommendationKey + "\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.recommendationMean.toString() + "\n"
                

                text = text.toString() + it.quoteSummary.result[0].financialData.currentPrice + "\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.currentRatio + "\n"
                text =
                    text.toString() + it.quoteSummary.result[0].financialData.targetMeanPrice + "\n"*/
            //}
        })

        setHasOptionsMenu(true)
    }

}