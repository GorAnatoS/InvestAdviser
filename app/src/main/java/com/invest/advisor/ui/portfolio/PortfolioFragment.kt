package com.invest.advisor.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.invest.advisor.R
import com.invest.advisor.data.db.userPortfolio.UserPortfolioEntry
import com.invest.advisor.databinding.FragmentPortfolioBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class PortfolioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingPortfolio =
            DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio, container, false)

        portfolioViewModel =
            ViewModelProvider(this).get(PortfolioViewModel::class.java)

        bindingPortfolio.userPortfolioViewModel = portfolioViewModel
        bindingPortfolio.lifecycleOwner = this

        portfolioViewModel.allData.observe(viewLifecycleOwner, Observer {
            bindingPortfolio.itemsContainer.adapter =
                GroupAdapter<GroupieViewHolder>().apply {
                    addAll(it.toPortfolioItemCards().sortedBy { it.itemContent.secId })
                }
        })

        return bindingPortfolio.root
    }

    private fun List<UserPortfolioEntry>.toPortfolioItemCards(): List<PortfolioItemCard> {
        return this.map {
            PortfolioItemCard(
                PortfolioItemContent(
                    it.secId,
                    it.secPrice,
                    it.secQuantity
                )
            )
        }
    }

    companion object {
        private lateinit var portfolioViewModel: PortfolioViewModel
        lateinit var bindingPortfolio: FragmentPortfolioBinding
    }
}