package com.invest.advisor.ui.portfolio.detailedPortfolioItem

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.invest.advisor.R
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.invest.advisor.data.network.YahooNetworkDataSourceImpl
import com.invest.advisor.data.network.yahooResponse.YahooApiService
import com.invest.advisor.ui.base.ScopedFragment
import com.invest.advisor.ui.portfolio.PortfolioViewModel
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

      /*  portfolioViewModel =
            ViewModelProvider(this).get(PortfolioViewModel::class.java)*/

        //findNavController().previousBackStackEntry?.savedStateHandle?.set("key", resultDeleted)

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

            text.apply {
                text = arguments?.getString("itemNumberInDB") + "\n"

                text = text.toString() + it.quoteSummary.result[0].price.shortName + "\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.currentPrice + "\n"
                text = text.toString() + it.quoteSummary.result[0].financialData.currentRatio + "\n"
                text =
                    text.toString() + it.quoteSummary.result[0].financialData.targetMeanPrice + "\n"
            }
        })

        setHasOptionsMenu(true)
    }

}