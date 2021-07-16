package com.invest.advisor.ui.moex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.invest.advisor.data.network.moexResponse.MoexNetworkDataSource


/**
 * Created by qsufff on 7/29/2020.
 */

class MoexViewModelFactory(
    private val moexNetworkDataSource: MoexNetworkDataSource
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoexViewModel(moexNetworkDataSource) as T
    }
}