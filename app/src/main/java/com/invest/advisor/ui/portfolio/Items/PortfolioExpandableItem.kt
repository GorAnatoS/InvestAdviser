package com.invest.advisor.ui.portfolio.Items

import android.graphics.drawable.Animatable
import android.view.View
import com.invest.advisor.R
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioEntry
import com.invest.advisor.databinding.PortfolioHeaderItemBinding
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem


class PortfolioExpandableItem(
    entry: UserPortfolioEntry,
    marketData: List<String>,
    val isExpandable: Boolean
) : PortfolioHeaderItem(
    entry,
    marketData
), ExpandableItem {

    var clickListener: ((PortfolioExpandableItem) -> Unit)? = null

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(binding: PortfolioHeaderItemBinding, position: Int) {
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

    private fun bindIcon(binding: PortfolioHeaderItemBinding) {
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
