package com.invest.advisor.ui.portfolio.portfolioItems

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.data.db.userPortfolio.UserPortfolioEntry
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.portfolio_header_item.*
import kotlin.math.roundToInt

open class HeaderItem(
    val entryDatabase: UserPortfolioEntry,
    val entryMarketData: List<String>,
    @DrawableRes private val iconResId: Int? = null,
    private val onIconClickListener: View.OnClickListener? = null
) : Item() {

    override fun getLayout(): Int {
        return R.layout.portfolio_header_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tvPurchaseDate.text = entryDatabase.secId
        viewHolder.tvPrice.text = entryMarketData[EnumMarketData.WAPRICE.ordinal] + "₽"
        viewHolder.tvQuantity.text = entryDatabase.secQuantity.toString() + " шт. ⋄"
        viewHolder.tvCurrentPrice.text = (entryMarketData[EnumMarketData.WAPRICE.ordinal].toDouble() * entryDatabase.secQuantity.toDouble()).toString()

        var currentPrice =
            (entryDatabase.secQuantity.toDouble() * entryMarketData[EnumMarketData.WAPRICE.ordinal].toDouble())
        currentPrice = (currentPrice * 100.0).roundToInt() / 100.0
        viewHolder.tvCurrentPrice.text = currentPrice.toString()

        var oldPrice = entryDatabase.secPrice.toDouble() * entryDatabase.secQuantity.toDouble()

        var changePcnt = (entryMarketData[EnumMarketData.WAPRICE.ordinal].toDouble() - entryDatabase.secPrice.toDouble()) / entryDatabase.secPrice.toDouble() * 100
        changePcnt = (changePcnt * 100.0).roundToInt() / 100.0

        var changePrice = currentPrice - oldPrice
        changePrice = (changePrice * 100.0).roundToInt() / 100.0

        if (changePcnt < 0)
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

        if (changePcnt == 0.0) viewHolder.tvCurrentPriceChng.setTextColor(
            ContextCompat.getColor(
                viewHolder.containerView.context,
                R.color.colorBlack
            )
        )

        viewHolder.tvCurrentPriceChng.text = "${changePrice} (${changePcnt})%"

        viewHolder.icon.apply {
            visibility = View.GONE
            iconResId?.let {
                visibility = View.VISIBLE
                setImageResource(it)
                setOnClickListener(onIconClickListener)
            }
        }
    }
}
