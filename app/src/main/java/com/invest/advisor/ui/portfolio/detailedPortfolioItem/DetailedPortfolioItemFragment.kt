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
import com.invest.advisor.internal.Helper
import com.invest.advisor.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.detailed_portfolio_item_fragment.*
import kotlinx.android.synthetic.main.detailed_portfolio_item_fragment.group_loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

private const val ARG_PARAM1 = "secId"

class DetailedPortfolioItemFragment : ScopedFragment(), KodeinAware{

   // var resultDeleted: Boolean = false
    override val kodein by closestKodein()
    private var secId: String? = null

    var hasOptionMenu = true

    companion object {
        lateinit var mYahooNetworkDataSource: YahooNetworkDataSourceImpl
        lateinit var mYahooApiService: YahooApiService


        //private lateinit var portfolioViewModel: PortfolioViewModel

        @JvmStatic
        fun newInstance(secId: String?) =
            DetailedPortfolioItemFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, secId)
                }
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            if ( it.getString(ARG_PARAM1) != null) {
                hasOptionMenu = false
            }

            secId = if ( it.getString(ARG_PARAM1) != null) {
                it.getString(ARG_PARAM1)
            }
            else { //from portfolio
                arguments?.getString("itemNumberInDB")
            }
        }
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
        Toast.makeText(context, getString(R.string.deleted), Toast.LENGTH_SHORT).show()


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mYahooApiService = YahooApiService(ConnectivityInterceptorImpl(requireContext()))
        mYahooNetworkDataSource = YahooNetworkDataSourceImpl(mYahooApiService)

        //launch(Dispatchers.Default) {  }
        launch(Dispatchers.Unconfined) {
            mYahooNetworkDataSource.fetchYahooData(secId + ".ME")//arguments?.getString("itemNumberInDB") + ".ME")
        }

        mYahooNetworkDataSource.downloadedYahooResponse.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            group_loading.visibility = View.GONE

            tvName.visibility = View.VISIBLE
            tvCurrentPrice.visibility = View.VISIBLE
            sliderDayPriceRange.visibility = View.VISIBLE
            tvRegularMarketDayLow.visibility = View.VISIBLE
            tvRegularMarketDayHigh.visibility = View.VISIBLE
            tvPrevClose.visibility = View.VISIBLE
            tvOpen.visibility = View.VISIBLE
            tvVolume.visibility = View.VISIBLE
            tvMarketCap.visibility = View.VISIBLE
            tvBeta.visibility = View.VISIBLE
            tvROE.visibility = View.VISIBLE
            tvROA.visibility = View.VISIBLE
            tvPrevCloseVal.visibility = View.VISIBLE
            tvOpenVal.visibility = View.VISIBLE
            tvVolumeVal.visibility = View.VISIBLE
            tvMarketCapVal.visibility = View.VISIBLE
            tvBetaVal.visibility = View.VISIBLE
            tvROEVal.visibility = View.VISIBLE
            tvROAVal.visibility = View.VISIBLE


            tvName.text = it.quoteSummary.result[0].price.shortName

            tvCurrentPrice.text =
                it.quoteSummary.result[0].price.regularMarketPrice.raw.toString() + " (" +
                        Helper.roundOffDecimal(it.quoteSummary.result[0].price.regularMarketChange.raw).toString() + ", " +
                        Helper.roundOffDecimal(it.quoteSummary.result[0].price.regularMarketChangePercent.raw).toString() + "%)"

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



            tvPrevCloseVal.text = it.quoteSummary.result[0].price.regularMarketPreviousClose.fmt
            tvOpenVal.text = it.quoteSummary.result[0].price.regularMarketOpen.fmt
            tvVolumeVal.text = it.quoteSummary.result[0].price.regularMarketVolume.fmt
            tvMarketCapVal.text = it.quoteSummary.result[0].price.marketCap.fmt
            tvBetaVal.text = it.quoteSummary.result[0].financialData.currentRatio.fmt
            tvROEVal.text = it.quoteSummary.result[0].financialData.returnOnEquity.fmt
            tvROAVal.text = it.quoteSummary.result[0].financialData.returnOnAssets.fmt




        })

        if (hasOptionMenu) setHasOptionsMenu(true)
    }

}