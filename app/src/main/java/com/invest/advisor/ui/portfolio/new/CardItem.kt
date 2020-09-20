package com.invest.advisor.ui.portfolio.new

import android.graphics.Color
import androidx.annotation.ColorInt
import com.invest.advisor.R
import com.invest.advisor.ui.portfolio.INSET
import com.invest.advisor.ui.portfolio.INSET_TYPE_KEY

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.item_card.*

open class CardItem (private val colorInt: String, val text: CharSequence? = "") : Item() {

    init {
        extras[INSET_TYPE_KEY] = INSET
    }

    override fun getLayout() = R.layout.item_card

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.root.setBackgroundColor(Color.parseColor(colorInt))
        viewHolder.text.text = text
    }
}
