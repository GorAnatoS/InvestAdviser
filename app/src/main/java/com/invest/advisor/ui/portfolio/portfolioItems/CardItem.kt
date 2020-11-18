package com.invest.advisor.ui.portfolio.portfolioItems

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.data.db.userPortfolio.UserPortfolioEntry
import com.invest.advisor.internal.Helper
import com.invest.advisor.ui.portfolio.INSET
import com.invest.advisor.ui.portfolio.INSET_TYPE_KEY
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.portfolio_card_item.icon
import kotlinx.android.synthetic.main.portfolio_card_item.tvCurrentPriceChng
import kotlinx.android.synthetic.main.portfolio_card_item.tvPrice
import kotlinx.android.synthetic.main.portfolio_card_item.tvPurchaseDate
import kotlinx.android.synthetic.main.portfolio_card_item.tvQuantity


open class CardItem(
    val entryDatabase: UserPortfolioEntry,
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
        viewHolder.tvPurchaseDate.text = Helper.getFormatedDateString(entryDatabase.secPurchaseDate)
        viewHolder.tvPrice.text = entryDatabase.secPrice + "₽"
        viewHolder.tvQuantity.text = entryDatabase.secQuantity.toString() + " шт. ⋄"

        var currentPrice =
            (entryDatabase.secQuantity.toDouble() * entryMarketData[EnumMarketData.WAPRICE.ordinal].toDouble())
        currentPrice = Math.round(currentPrice * 100.0) / 100.0

        var oldPrice = entryDatabase.secPrice.toDouble() * entryDatabase.secQuantity.toDouble()

        var changePcnt =
            (entryMarketData[EnumMarketData.WAPRICE.ordinal].toDouble() - entryDatabase.secPrice.toDouble()) / entryDatabase.secPrice.toDouble() * 100
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

