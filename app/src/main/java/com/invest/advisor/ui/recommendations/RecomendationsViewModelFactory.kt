package com.invest.advisor.ui.analitics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.invest.advisor.data.network.YahooNetworkDataSource


/**
 * Created by qsufff on 7/29/2020.
 */

class RecomendationsViewModelFactory (
    private val yahooNetworkDataSource: YahooNetworkDataSource
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  RecommendationsViewModel(yahooNetworkDataSource) as T
    }
}