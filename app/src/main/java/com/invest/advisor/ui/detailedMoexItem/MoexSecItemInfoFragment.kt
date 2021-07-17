package com.invest.advisor.ui.detailedMoexItem

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import com.invest.advisor.R
import com.invest.advisor.data.network.ConnectivityInterceptor
import com.invest.advisor.data.network.yahooResponse.YahooNetworkDataSourceImpl
import com.invest.advisor.data.network.yahooResponse.YahooApiService
import com.invest.advisor.databinding.DetailedPortfolioItemFragmentBinding
import com.invest.advisor.internal.Constants.ARG_PARAM_SECID
import com.invest.advisor.internal.MathHelper
import com.invest.advisor.ui.base.ScopedFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

/**
 * Fragment contains main info about selected security [ARG_PARAM_SECID]
 */

class MoexSecItemInfoFragment : ScopedFragment(), KodeinAware {

    lateinit var binding: DetailedPortfolioItemFragmentBinding

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
            MoexSecItemInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_SECID, secId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            if (it.getString(ARG_PARAM_SECID) != null) {
                hasOptionMenu = false
            }

            secId = if (it.getString(ARG_PARAM_SECID) != null) {
                it.getString(ARG_PARAM_SECID)
            } else { //from portfolio
                arguments?.getString("itemNumberInDB")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.detailed_portfolio_item_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detailed_portfolio_item_menu, menu)

        val deleteItemFromDB = menu.findItem(R.id.delete_item_from_db)

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

        mYahooApiService = YahooApiService(ConnectivityInterceptor(requireContext()))
        mYahooNetworkDataSource = YahooNetworkDataSourceImpl(mYahooApiService)

        //launch(Dispatchers.Default) {  }
        launch(Dispatchers.Unconfined) {
            mYahooNetworkDataSource.fetchYahooData(secId + ".ME")//arguments?.getString("itemNumberInDB") + ".ME")
        }

        mYahooNetworkDataSource.downloadedYahooResponse.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            binding.groupLoading.visibility = View.GONE

            binding.tvName.visibility = View.VISIBLE
            binding.tvCurrentPrice.visibility = View.VISIBLE
            binding.sliderDayPriceRange.visibility = View.VISIBLE
            binding.tvRegularMarketDayLow.visibility = View.VISIBLE
            binding.tvRegularMarketDayHigh.visibility = View.VISIBLE
            binding.tvPrevClose.visibility = View.VISIBLE
            binding.tvOpen.visibility = View.VISIBLE
            binding.tvVolume.visibility = View.VISIBLE
            binding.tvMarketCap.visibility = View.VISIBLE
            binding.tvBeta.visibility = View.VISIBLE
            binding.tvROE.visibility = View.VISIBLE
            binding.tvROA.visibility = View.VISIBLE
            binding.tvPrevCloseVal.visibility = View.VISIBLE
            binding.tvOpenVal.visibility = View.VISIBLE
            binding.tvVolumeVal.visibility = View.VISIBLE
            binding.tvMarketCapVal.visibility = View.VISIBLE
            binding.tvBetaVal.visibility = View.VISIBLE
            binding.tvROEVal.visibility = View.VISIBLE
            binding.tvROEVal.visibility = View.VISIBLE


            binding.tvName.text = it.quoteSummary.result[0].price.shortName

            binding.tvCurrentPrice.text =
                it.quoteSummary.result[0].price.regularMarketPrice.raw.toString() + " (" +
                        MathHelper.roundOffDecimal(it.quoteSummary.result[0].price.regularMarketChange.raw)
                            .toString() + ", " +
                        MathHelper.roundOffDecimal(it.quoteSummary.result[0].price.regularMarketChangePercent.raw)
                            .toString() + "%)"

            binding.sliderDayPriceRange.apply {
                valueFrom = it.quoteSummary.result[0].price.regularMarketDayLow.raw.toFloat()
                valueTo = it.quoteSummary.result[0].price.regularMarketDayHigh.raw.toFloat()
                value = it.quoteSummary.result[0].price.regularMarketPrice.raw.toFloat()



                binding.tvRegularMarketDayHigh.text =
                    it.quoteSummary.result[0].price.regularMarketDayHigh.raw.toString()
                binding.tvRegularMarketDayLow.text =
                    it.quoteSummary.result[0].price.regularMarketDayLow.raw.toString()
            }

            binding.sliderDayPriceRange.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: Slider) {
                    //println("Start Tracking Touch")
                }

                override fun onStopTrackingTouch(slider: Slider) {
                    binding.sliderDayPriceRange.value =
                        it.quoteSummary.result[0].price.regularMarketPrice.raw.toFloat()
                }
            })

            // TODO: 11/16/2020 изменить бэкграунд при нажджатии на слайдер

            binding.tvPrevCloseVal.text = it.quoteSummary.result[0].price.regularMarketPreviousClose.fmt
            binding.tvOpenVal.text = it.quoteSummary.result[0].price.regularMarketOpen.fmt
            binding.tvVolumeVal.text = it.quoteSummary.result[0].price.regularMarketVolume.fmt
            binding.tvMarketCapVal.text = it.quoteSummary.result[0].price.marketCap.fmt
            binding.tvBetaVal.text = it.quoteSummary.result[0].financialData.currentRatio.fmt
            binding.tvROEVal.text = it.quoteSummary.result[0].financialData.returnOnEquity.fmt
            binding.tvROAVal.text = it.quoteSummary.result[0].financialData.returnOnAssets.fmt
        })

        if (hasOptionMenu) setHasOptionsMenu(true)
    }
}