package com.invest.advisor.ui.portfolio.items

import android.graphics.drawable.Animatable
import android.view.View
import com.invest.advisor.R
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioEntry
import com.invest.advisor.databinding.ItemPortfolioExpandableHeaderBinding
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem

/**
 * CardView item where we accumulate all [PortfolioCardItem] of the same security in one expandable header item on base
 * of [PortfolioCardHeaderItem]
 */

class PortfolioCardExpandableItem(
    entry: UserPortfolioEntry,
    marketData: List<String>,
    val isExpandable: Boolean
) : PortfolioCardHeaderItem(
    entry,
    marketData
), ExpandableItem {

    var clickListener: ((PortfolioCardExpandableItem) -> Unit)? = null

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(binding: ItemPortfolioExpandableHeaderBinding, position: Int) {
        super.bind(binding, position)

        // Initial icon state -- not animated.
        binding.icon.apply {
            if (isExpandable) {
                visibility = View.VISIBLE
                setImageResource(if (expandableGroup.isExpanded) R.drawable.collapse_animated else R.drawable.expand_animated)
                setOnClickListener {
                    expandableGroup.onToggleExpanded()
                    bindIcon(binding)
                }
            } else visibility = View.INVISIBLE
        }

        binding.root.setOnClickListener {
            clickListener?.invoke(this)
        }
    }

    private fun bindIcon(binding: ItemPortfolioExpandableHeaderBinding) {
        binding.icon.apply {
            visibility = View.VISIBLE
            setImageResource(if (expandableGroup.isExpanded) R.drawable.collapse_animated else R.drawable.expand_animated)
            (drawable as Animatable).start()
        }
    }

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }
}