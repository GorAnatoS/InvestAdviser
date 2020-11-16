package com.invest.advisor.ui.moex.detailedMoexItem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.invest.advisor.R
import com.invest.advisor.databinding.FragmentCommonDetailedMoexItemBinding
import com.invest.advisor.ui.portfolio.detailedPortfolioItem.DetailedPortfolioItemFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


private const val ARG_PARAM1 = "secId"
private const val ARG_PARAM2 = "secPrice"

/**
 * A simple [Fragment] subclass.
 * Use the [CommonDetailedMoexItem.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommonDetailedMoexItem : Fragment() {

    private lateinit var binding: FragmentCommonDetailedMoexItemBinding

    // TODO: Rename and change types of parameters
    private var secId: String? = null
    private var secPrice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            secId = it.getString(ARG_PARAM1)
            secPrice = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_common_detailed_moex_item, container, false)

        binding.pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 2
            }

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> DetailedMoexItemFragment.newInstance(secId, secPrice!!)
                    1 -> DetailedPortfolioItemFragment.newInstance()
                    else -> DetailedMoexItemFragment()
                }
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> "Купить"
                1 -> "Значения"
                else -> "Купить"
            }
        }.attach()

    }

}