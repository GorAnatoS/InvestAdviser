package com.invest.advisor.ui.moex.detailedMoexItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.invest.advisor.R
import com.invest.advisor.ui.base.ScopedFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class DetailedMoexItemFragment : ScopedFragment(), KodeinAware{
    override val kodein by closestKodein()
    private val viewModelFactory: DetailedMoexItemFactory by instance()

    private lateinit var viewModel: DetailedMoexItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detailed_moex_item, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailedMoexItemViewModel::class.java)
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val marketData = viewModel.marketData.await()
        val seciritiesData = viewModel.securities.await()
    }
}