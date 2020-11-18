package com.invest.advisor.ui.analitics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.invest.advisor.data.network.YahooNetworkDataSource
import com.invest.advisor.ui.recommendations.RecommendationsViewModel


/**
 * Created by qsufff on 7/29/2020.
 */

class AnaliticsViewModelFactory (
    private val yahooNetworkDataSource: YahooNetworkDataSource
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  AnaliticsViewModel(yahooNetworkDataSource) as T
    }
}