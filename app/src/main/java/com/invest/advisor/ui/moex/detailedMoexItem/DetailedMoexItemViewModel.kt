package com.invest.advisor.ui.moex.detailedMoexItem

import androidx.lifecycle.ViewModel
import com.invest.advisor.data.network.MoexNetworkDataSource
import com.invest.advisor.data.repository.MoexRepository
import com.invest.advisor.internal.lazyDeferred

class DetailedMoexItemViewModel(
    private val moexNetworkDataSource: MoexNetworkDataSource
) : ViewModel() {

    val securities by lazyDeferred {
        moexNetworkDataSource.fetchSecurities()
    }
    val marketData by lazyDeferred {
        moexNetworkDataSource.fetchSecurities()
    }
}