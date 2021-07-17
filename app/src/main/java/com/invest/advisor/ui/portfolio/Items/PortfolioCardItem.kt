package com.invest.advisor.ui.portfolio.Items

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.invest.advisor.R
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioEntry
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.databinding.PortfolioCardItemBinding
import com.invest.advisor.internal.DateHelper.Companion.getFormattedDateString
import com.invest.advisor.ui.portfolio.INSET
import com.invest.advisor.ui.portfolio.INSET_TYPE_KEY
import com.xwray.groupie.viewbinding.BindableItem

/*

 */

class PortfolioCardItem(
    val database: UserPortfolioEntry,
    val entryMarketData: List<String>,
    @DrawableRes private val iconResId: Int? = null,
    private val onIconClickListener: View.OnClickListener? = null
): BindableItem<PortfolioCardItemBinding>() {

    lateinit var binding: PortfolioCardItemBinding

    init {
        extras[INSET_TYPE_KEY] = INSET
    }

    override fun getLayout(): Int {
        return R.layout.portfolio_card_item
    }

    override fun initializeViewBinding(view: View): PortfolioCardItemBinding {
        return PortfolioCardItemBinding.bind(view)
    }

    override fun bind(binding: PortfolioCardItemBinding, position: Int) {
        binding.tvPurchaseDate.text = getFormattedDateString(database.secPurchaseDate)
        binding.tvPrice.text = database.secPrice + "₽"
        binding.tvQuantity.text = database.secQuantity.toString() + " шт. ⋄"

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
            binding.tvCurrentPriceChng.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.shareMinusColor
                )
            )
        else
            binding.tvCurrentPriceChng.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.sharePlusColor
                )
            )

        if (changePcnt == 0.0) binding.tvCurrentPriceChng.setTextColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.black
            )
        )

        binding.tvCurrentPriceChng.text = "${changePrice} (${changePcnt})%"

        binding.icon.apply {
            visibility = View.GONE
            iconResId?.let {
                visibility = View.VISIBLE
                setImageResource(it)
                setOnClickListener(onIconClickListener)
            }
        }
    }
}