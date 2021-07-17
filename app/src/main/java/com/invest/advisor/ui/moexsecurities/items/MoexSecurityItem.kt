package com.invest.advisor.ui.moexsecurities.items

import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.MoexEntry
import com.invest.advisor.databinding.ItemMoexSecurityBinding
import com.invest.advisor.ui.moexsecurities.MoexSecuritiesListFragmentDirections
import com.xwray.groupie.viewbinding.BindableItem

/**
 * Created by qsufff on 7/30/2020.
 */

class MoexSecurityItem(
    val moexEntry: MoexEntry
) : BindableItem<ItemMoexSecurityBinding>() {

    override fun initializeViewBinding(view: View): ItemMoexSecurityBinding {
        return ItemMoexSecurityBinding.bind(view)
    }

    override fun bind(binding: ItemMoexSecurityBinding, position: Int) {
        binding.apply {
            tvPurchaseDate.text = moexEntry.secName
            tvSecId.text = moexEntry.secId
            tvPrice.text = moexEntry.secPrice


            tvChange.text = moexEntry.secChange
            if (moexEntry.secChange?.startsWith("-")!!) {
                tvChange.setTextColor(ContextCompat.getColor(binding.root.context, R.color.shareMinusColor))
            } else {
                tvChange.setTextColor(ContextCompat.getColor(binding.root.context, R.color.sharePlusColor))
            }
            if (moexEntry.secChange == "0") tvChange.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.black
                )
            )


            tvChangePcnt.text = "(${moexEntry.secChangePcnt}%)"
            if (moexEntry.secChangePcnt?.startsWith("-")!!) {
                tvChangePcnt.setTextColor(ContextCompat.getColor(binding.root.context, R.color.shareMinusColor))
            } else tvChangePcnt.setTextColor(ContextCompat.getColor(binding.root.context, R.color.sharePlusColor))
            if (moexEntry.secChangePcnt == "0") tvChangePcnt.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.black
                )
            )
        }

        binding.root.setOnClickListener {
            //Toast.makeText(viewHolder.containerView.context,  , Toast.LENGTH_SHORT).show()
            if (moexEntry.secPrice != "NoE")
                it.findNavController().navigate(
                    MoexSecuritiesListFragmentDirections.actionMoexFragmentToCommonDetailedMoexItem(
                        moexEntry.secId,
                        moexEntry.secPrice!!
                    )
                )
        }
    }


    override fun getLayout() = R.layout.item_moex_security
}