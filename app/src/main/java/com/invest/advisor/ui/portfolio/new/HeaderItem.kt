package com.invest.advisor.ui.portfolio.new

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.invest.advisor.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.portfolio_item.*

open class HeaderItem(
    val secId: String = "",
    val secPrice: String = "",
    val secQuantity: Int = 0,
    val secCurrentPrice: String = "",
    val changePcnt: String = "",
    val changePrice: Double = 0.0,
    @DrawableRes private val iconResId: Int? = null,
    private val onIconClickListener: View.OnClickListener? = null
) : Item() {

    override fun getLayout(): Int {
        //return R.layout.item_header
        return R.layout.portfolio_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tvSecName.text = secId
        viewHolder.tvPrice.text = secPrice
        viewHolder.tvQuantity.text = secQuantity.toString()
        viewHolder.tvCurrentPrice.text = secCurrentPrice



        if (changePcnt.startsWith("-"))
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

        if (changePcnt == "0") viewHolder.tvCurrentPriceChng.setTextColor(
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
