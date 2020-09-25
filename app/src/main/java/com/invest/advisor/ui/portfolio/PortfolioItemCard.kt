package com.invest.advisor.ui.portfolio

import androidx.core.content.ContextCompat
import com.invest.advisor.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.portfolio_header_item.*

/**
 * Created by qsufff on 9/13/2020.
 */

class PortfolioItemCard(private val content: PortfolioItemContent) : Item() {
    var itemContent: PortfolioItemContent = content

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tvPurchaseDate.text = content.secId
        viewHolder.tvPrice.text = content.secPrice
        viewHolder.tvQuantity.text = content.secQuantity.toString()
        viewHolder.tvCurrentPrice.text = content.secCurrentPrice



        if (content.changePcnt.startsWith("-"))
            viewHolder.tvCurrentPriceChng.setTextColor(
                ContextCompat.getColor(
                    viewHolder.containerView.context,
                    R.color.shareMinusColor
                )
            )
        else
            viewHolder.tvCurrentPriceChng.setTextColor(
                ContextCompat.getColor(
                    viewHolder.containerView.context,
                    R.color.sharePlusColor
                )
            )

        if (content.changePcnt == "0") viewHolder.tvCurrentPriceChng.setTextColor(
            ContextCompat.getColor(
                viewHolder.containerView.context,
                R.color.colorBlack
            )
        )

        viewHolder.tvCurrentPriceChng.text = "${content.changePrice} (${content.changePcnt})%"



    }

    override fun getLayout() = R.layout.portfolio_header_item
}

data class PortfolioItemContent(
    val secId: String = "",
    val secPrice: String = "",
    val secQuantity: Int = 0,
    val secCurrentPrice: String = "",
    val changePcnt: String = "",
    val changePrice: Double = 0.0
)