package com.invest.advisor.ui.moex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.invest.advisor.data.repository.MoexRepository


/**
 * Created by qsufff on 7/29/2020.
 */

class MoexViewModelFactory (
    private val moexRepository: MoexRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  MoexViewModel(moexRepository) as T
    }
}