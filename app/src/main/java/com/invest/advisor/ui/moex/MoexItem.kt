package com.invest.advisor.ui.moex

import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.MoexEntry
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_moex.*

/**
 * Created by qsufff on 7/30/2020.
 */

class MoexItem(
    val moexEntry: MoexEntry
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            tvPurchaseDate.text = moexEntry.secName
            tvSecId.text = moexEntry.secId
            tvPrice.text = moexEntry.secPrice


            tvChange.text = moexEntry.secChange
            if (moexEntry.secChange?.startsWith("-")!!) {
                tvChange.setTextColor(ContextCompat.getColor(containerView.context, R.color.shareMinusColor))
            } else {
                tvChange.setTextColor(ContextCompat.getColor(containerView.context, R.color.sharePlusColor))
            }
            if (moexEntry.secChange == "0") tvChange.setTextColor(ContextCompat.getColor(containerView.context, R.color.colorBlack))


            tvChangePcnt.text = "(${moexEntry.secChangePcnt}%)"
            if (moexEntry.secChangePcnt?.startsWith("-")!!) {
                tvChangePcnt.setTextColor(ContextCompat.getColor(containerView.context, R.color.shareMinusColor))
            } else tvChangePcnt.setTextColor(ContextCompat.getColor(containerView.context, R.color.sharePlusColor))
            if (moexEntry.secChangePcnt == "0") tvChangePcnt.setTextColor(ContextCompat.getColor(containerView.context, R.color.colorBlack))

            itemView.setOnClickListener{
                //Toast.makeText(viewHolder.containerView.context,  , Toast.LENGTH_SHORT).show()
                if (moexEntry.secPrice != "NoE")
                    it.findNavController().navigate(MoexFragmentDirections.actionMoexFragmentToMoexDetail(moexEntry.secId, moexEntry.secPrice!!))
            }
        }
    }

    override fun getLayout() = R.layout.item_moex
}