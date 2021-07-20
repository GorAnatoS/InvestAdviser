package com.invest.advisor.ui.detailedMoexItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.invest.advisor.R
import com.invest.advisor.data.repository.UserPortfolioRepository
import com.invest.advisor.databinding.FragmentDetailedMoexSecurityItemBinding
import com.invest.advisor.internal.Constants.ARG_PARAM_SECID
import com.invest.advisor.internal.Constants.ARG_PARAM_SECPRICE
import com.invest.advisor.ui.base.ScopedFragment

/**
 * Fragment that contains [ViewPager2] and has pages:
 * [AddMoexSecItemFragment] - Fragment to add new security to [UserPortfolioRepository]
 * [MoexSecItemInfoFragment] - Fragment contains main info about selected security
 */
class DetailedMoexSecurityItemFragment : ScopedFragment() {

    private lateinit var binding: FragmentDetailedMoexSecurityItemBinding

    private var secId: String? = null
    private var secPrice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            secId = it.getString(ARG_PARAM_SECID)
            secPrice = it.getString(ARG_PARAM_SECPRICE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detailed_moex_security_item, container, false)

        binding.pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 2
            }

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> AddMoexSecItemFragment.newInstance(secId, secPrice!!)
                    1 -> MoexSecItemInfoFragment.newInstance(secId)
                    else -> AddMoexSecItemFragment.newInstance(secId, secPrice!!)
                }
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> "Добавить"
                1 -> "Показатели"
                else -> "Купить"
            }
        }.attach()
    }
}