package com.invest.advisor.ui.recommendations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.invest.advisor.data.network.MoexNetworkDataSource
import com.invest.advisor.data.network.YahooNetworkDataSource


/**
 * Created by qsufff on 7/29/2020.
 */

class RecomendationsViewModelFactory (
    private val yahooNetworkDataSource: YahooNetworkDataSource,
    private val moexNetworkDataSource: MoexNetworkDataSource

): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  RecommendationsViewModel(yahooNetworkDataSource, moexNetworkDataSource) as T
    }
}