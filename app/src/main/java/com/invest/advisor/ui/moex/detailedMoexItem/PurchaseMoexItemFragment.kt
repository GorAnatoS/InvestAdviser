package com.invest.advisor.ui.moex.detailedMoexItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.invest.advisor.R
import com.invest.advisor.data.db.userPortfolio.UserPortfolioEntry
import com.invest.advisor.databinding.FragmentMoexDetailBinding
import com.invest.advisor.databinding.FragmentMoexDetailPagerBinding
import com.invest.advisor.ui.base.ScopedFragment
import com.invest.advisor.ui.portfolio.PortfolioViewModel
import kotlinx.android.synthetic.main.fragment_moex_detail.*
import kotlinx.android.synthetic.main.fragment_moex_detail_pager.*
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "secId"
private const val ARG_PARAM2 = "secPrice"

class DetailedMoexItemFragment : ScopedFragment() {
    private lateinit var rootView: FragmentMoexDetailBinding
    private lateinit var viewModel: PortfolioViewModel


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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_moex_detail,
                container,
                false
            )

        viewModel =
            ViewModelProvider(requireActivity()).get(PortfolioViewModel::class.java)

        rootView.apply {
            textTitle.text =
                textTitle.text.toString()
                    .replace("ХХ", secId!!, true)
            editTextPrice.setText(secPrice!!)

            textViewTotalMoneyIs.text =
                (editTextQuantity.text.toString()
                    .toDouble() * rootView.editTextPrice.text.toString()
                    .toDouble()).toString()

            editTextPrice.doAfterTextChanged {
                if (editTextPrice.text.isNotEmpty() && editTextQuantity.text.isNotEmpty())
                    textViewTotalMoneyIs.text = (rootView.editTextQuantity.text.toString()
                        .toDouble() * editTextPrice.text.toString()
                        .toDouble()).toString()
            }

            editTextQuantity.doAfterTextChanged {
                if (editTextPrice.text.isNotEmpty() && editTextQuantity.text.isNotEmpty())
                    textViewTotalMoneyIs.text = (editTextQuantity.text.toString()
                        .toDouble() * editTextPrice.text.toString()
                        .toDouble()).toString()
            }

            addButton.setOnClickListener {
                if (editTextPrice.text.isNotEmpty() && editTextQuantity.text.isNotEmpty()) {
                    textViewTotalMoneyIs.text =
                        (editTextQuantity.text.toString()
                            .toDouble() * editTextPrice.text.toString()
                            .toDouble()).toString()

                    val date = Calendar.getInstance().time
                    val formatter =
                        SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
                    val formatedDate = formatter.format(date)


                    val newUserPortfolioEntry = UserPortfolioEntry(
                        0,
                        secId!!,
                        editTextPrice.text.toString(),
                        editTextQuantity.text.toString().toInt(),
                        formatedDate
                    )
                    viewModel.insert(newUserPortfolioEntry)
                    Toast.makeText(
                        requireContext(),
                        newUserPortfolioEntry.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                } else
                    Toast.makeText(
                        requireContext(),
                        "Ошибка ввода",
                        Toast.LENGTH_LONG
                    ).show()

                findNavController().navigateUp()

            }
        }
        return rootView.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommonDetailedMoexItem.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(secId: String?, secPrice: String) =
            DetailedMoexItemFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, secId)
                    putString(ARG_PARAM2, secPrice)
                }
            }
    }
}