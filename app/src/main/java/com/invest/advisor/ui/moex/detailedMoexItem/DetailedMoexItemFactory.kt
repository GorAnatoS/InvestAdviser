package com.invest.advisor.ui.moex.detailedMoexItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.invest.advisor.data.network.MoexNetworkDataSource
import com.invest.advisor.data.repository.MoexRepository


/**
 * Created by qsufff on 7/29/2020.
 */

class DetailedMoexItemFactory (
    private val moexNetworkDataSource: MoexNetworkDataSource
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  DetailedMoexItemViewModel(moexNetworkDataSource) as T
    }
}