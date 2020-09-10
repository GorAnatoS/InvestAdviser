package com.invest.advisor.ui.moex

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.invest.advisor.R
import com.invest.advisor.data.db.entity.MoexEntry
import com.invest.advisor.ui.MainActivity
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_moex.*


/**
 * Created by qsufff on 7/30/2020.
 */

class MoexItem(
    val moexEntry: MoexEntry
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            tvSecId.text = moexEntry.secId
            tvSecName.text = moexEntry.secName
            tvPrice.text = moexEntry.secPrice
            tvChange.text = moexEntry.secChange

            itemView.setOnClickListener{
                Toast.makeText(viewHolder.containerView.context,  moexEntry.secId, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getLayout() = R.layout.item_moex
}