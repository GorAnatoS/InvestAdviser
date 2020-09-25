package com.invest.advisor.ui.portfolio.portfolioItems

import android.graphics.drawable.Animatable
import android.view.View
import com.invest.advisor.R
import com.invest.advisor.data.db.userPortfolio.UserPortfolioEntry
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.portfolio_header_item.*


class ExpandablePortfolioItem(
    entry: UserPortfolioEntry,
    marketData: List<String>,
    val isExpandable: Boolean
) : HeaderItem(
    entry,
    marketData
), ExpandableItem {

    var clickListener: ((ExpandablePortfolioItem) -> Unit)? = null

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        super.bind(viewHolder, position)

        // Initial icon state -- not animated.
        viewHolder.icon.apply {
            if (isExpandable) {
                visibility = View.VISIBLE
                setImageResource(if (expandableGroup.isExpanded) R.drawable.collapse_animated else R.drawable.expand_animated)
                setOnClickListener {
                    expandableGroup.onToggleExpanded()
                    bindIcon(viewHolder)
                }
            }
            else visibility = View.INVISIBLE
        }

        viewHolder.itemView.setOnClickListener {
            clickListener?.invoke(this)
        }
    }

    private fun bindIcon(viewHolder: GroupieViewHolder) {
        viewHolder.icon.apply {
            visibility = View.VISIBLE
            setImageResource(if (expandableGroup.isExpanded) R.drawable.collapse_animated else R.drawable.expand_animated)
            (drawable as Animatable).start()
        }
    }

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }
}
