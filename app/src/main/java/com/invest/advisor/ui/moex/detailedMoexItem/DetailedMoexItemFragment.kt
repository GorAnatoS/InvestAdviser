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

class DetailedMoexItemFragment : ScopedFragment() {

    private lateinit var rootView: FragmentMoexDetailBinding
    private lateinit var viewModel: PortfolioViewModel


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

        rootView.apply {
            textTitle.text =
                textTitle.text.toString()
                    .replace("ХХ", arguments?.getString("secId")!!, true)
            editTextPrice.setText(arguments?.getString("secPrice"))

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

                    viewModel =
                        ViewModelProvider(requireActivity()).get(PortfolioViewModel::class.java)
                    val newUserPortfolioEntry = UserPortfolioEntry(
                        0,
                        arguments?.getString("secId")!!,
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        text_title.text = text_title.text.toString().replace("ХХ", arguments?.getString("secId")!!, true)

        editText_price.setText(arguments?.getString("secPrice"))

        textView_totalMoney_is.text = (editText_quantity.text.toString().toDouble() * editText_price.text.toString().toDouble()).toString()

        editText_price.doAfterTextChanged {
            if (editText_price.text.isNotEmpty() && editText_quantity.text.isNotEmpty())
                textView_totalMoney_is.text = (editText_quantity.text.toString().toDouble() * editText_price.text.toString().toDouble()).toString()
        }

        editText_quantity.doAfterTextChanged {
            if (editText_price.text.isNotEmpty() && editText_quantity.text.isNotEmpty())
                textView_totalMoney_is.text = (editText_quantity.text.toString().toDouble() * editText_price.text.toString().toDouble()).toString()
        }

        addButton.setOnClickListener{
            if (editText_price.text.isNotEmpty() && editText_quantity.text.isNotEmpty()) {
                textView_totalMoney_is.text =
                    (editText_quantity.text.toString().toDouble() * editText_price.text.toString()
                        .toDouble()).toString()

                val date = Calendar.getInstance().time
                val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
                val formatedDate = formatter.format(date)

                viewModel = ViewModelProvider(this).get(PortfolioViewModel::class.java)
                val newUserPortfolioEntry = UserPortfolioEntry(
                    0,
                    arguments?.getString("secId")!!,
                    editText_price.text.toString(),
                    editText_quantity.text.toString().toInt(),
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
}