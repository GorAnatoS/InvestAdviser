package com.invest.advisor.ui.portfolio.portfolioItems

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.invest.advisor.R
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioEntry
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.internal.DateHelper.Companion.getFormattedDateString
import com.invest.advisor.internal.MathHelper
import com.invest.advisor.ui.portfolio.INSET
import com.invest.advisor.ui.portfolio.INSET_TYPE_KEY
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.portfolio_card_item.*


open class CardItem(
    val database: UserPortfolioEntry,
    val entryMarketData: List<String>,
    @DrawableRes private val iconResId: Int? = null,
    private val onIconClickListener: View.OnClickListener? = null
) : Item() {

    init {
        extras[INSET_TYPE_KEY] = INSET
    }

    override fun getLayout(): Int {
        return R.layout.portfolio_card_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tvPurchaseDate.text = getFormattedDateString(database.secPurchaseDate)
        viewHolder.tvPrice.text = database.secPrice + "₽"
        viewHolder.tvQuantity.text = database.secQuantity.toString() + " шт. ⋄"

        var currentPrice =
            (database.secQuantity.toDouble() * entryMarketData[EnumMarketData.WAPRICE.ordinal].toDouble())
        currentPrice = Math.round(currentPrice * 100.0) / 100.0

        var oldPrice = database.secPrice.toDouble() * database.secQuantity.toDouble()

        var changePcnt =
            (entryMarketData[EnumMarketData.WAPRICE.ordinal].toDouble() - database.secPrice.toDouble()) / database.secPrice.toDouble() * 100
        changePcnt = Math.round(changePcnt * 100.0) / 100.0

        var changePrice = currentPrice - oldPrice
        changePrice = Math.round(changePrice * 100.0) / 100.0

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
                R.color.black
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