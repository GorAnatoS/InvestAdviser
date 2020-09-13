package com.invest.advisor.ui.portfolio

import com.invest.advisor.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.portfolio_item.*

/**
 * Created by qsufff on 9/13/2020.
 */

class PortfolioItemCard(private val content: PortfolioItemContent) : Item() {
    var itemContent: PortfolioItemContent = content

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tvSecId.text = content.secId
        viewHolder.tvPrice.text = content.secPrice
        viewHolder.tvQuantity.text = content.secQuantity.toString()
    }

    override fun getLayout() = R.layout.portfolio_item
}

data class PortfolioItemContent(
    val secId: String = "",
    val secPrice: String = "",
    val secQuantity: Int = 0
)