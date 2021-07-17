package com.invest.advisor.ui.views.portfolioItems

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.invest.advisor.R
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioEntry
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.databinding.PortfolioHeaderItemBinding
import com.xwray.groupie.viewbinding.BindableItem
import kotlin.math.roundToInt

open class PortfolioHeaderItem(
    val database: UserPortfolioEntry,
    val entryMarketData: List<String>,
    @DrawableRes private val iconResId: Int? = null,
    private val onIconClickListener: View.OnClickListener? = null
) : BindableItem<PortfolioHeaderItemBinding>() {

    override fun getLayout(): Int {
        return R.layout.portfolio_header_item
    }

    override fun initializeViewBinding(view: View): PortfolioHeaderItemBinding {
        return PortfolioHeaderItemBinding.bind(view)
    }

    override fun bind(binding: PortfolioHeaderItemBinding, position: Int) {
        binding.tvPurchaseDate.text = database.secId
        binding.tvPrice.text = entryMarketData[EnumMarketData.WAPRICE.ordinal] + "₽"
        binding.tvQuantity.text = database.secQuantity.toString() + " шт. ⋄"
        binding.tvCurrentPrice.text =
            (entryMarketData[EnumMarketData.WAPRICE.ordinal].toDouble() * database.secQuantity.toDouble()).toString()

        var currentPrice =
            (database.secQuantity.toDouble() * entryMarketData[EnumMarketData.WAPRICE.ordinal].toDouble())
        currentPrice = (currentPrice * 100.0).roundToInt() / 100.0
        binding.tvCurrentPrice.text = currentPrice.toString()

        var oldPrice = database.secPrice.toDouble() * database.secQuantity.toDouble()

        var changePcnt =
            (entryMarketData[EnumMarketData.WAPRICE.ordinal].toDouble() - database.secPrice.toDouble()) / database.secPrice.toDouble() * 100
        changePcnt = (changePcnt * 100.0).roundToInt() / 100.0

        var changePrice = currentPrice - oldPrice
        changePrice = (changePrice * 100.0).roundToInt() / 100.0

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
