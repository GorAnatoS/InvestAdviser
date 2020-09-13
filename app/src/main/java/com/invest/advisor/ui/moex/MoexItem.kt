package com.invest.advisor.ui.moex

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
            tvSecId.text = moexEntry.secId
            tvSecName.text = moexEntry.secName
            tvPrice.text = moexEntry.secPrice
            tvQuantity.text = moexEntry.secChange

            itemView.setOnClickListener{
                //Toast.makeText(viewHolder.containerView.context,  , Toast.LENGTH_SHORT).show()
                it.findNavController().navigate(MoexFragmentDirections.actionMoexFragmentToMoexDetail(moexEntry.secId, moexEntry.secPrice!!))
            }
        }
    }

    override fun getLayout() = R.layout.item_moex
}