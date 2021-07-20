package com.invest.advisor.ui.detailedMoexItem

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
import com.google.android.material.datepicker.MaterialDatePicker
import com.invest.advisor.R
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioEntry
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioDatabase
import com.invest.advisor.databinding.FragmentAddMoexSecItemBinding
import com.invest.advisor.internal.Constants.ARG_PARAM_SECID
import com.invest.advisor.internal.Constants.ARG_PARAM_SECPRICE
import com.invest.advisor.internal.DateHelper.Companion.formattedDateStringToFormattedDateLong
import com.invest.advisor.internal.DateHelper.Companion.getFormattedDateString
import com.invest.advisor.ui.portfolio.PortfolioViewModel

/**
 * Fragment to add new security [UserPortfolioEntry] to [UserPortfolioDatabase] with
 * [FragmentAddMoexSecItemBinding.editTextQuantity] and [FragmentAddMoexSecItemBinding.editTextPrice]
 */

class AddMoexSecItemFragment : Fragment() {
    private lateinit var binding: FragmentAddMoexSecItemBinding
    private lateinit var viewModel: PortfolioViewModel

    private var secId: String? = null
    private var secPrice: String? = null

    private var formattedDateLong: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =
            ViewModelProvider(requireActivity()).get(PortfolioViewModel::class.java)

        arguments?.let {
            secId = it.getString(ARG_PARAM_SECID)
            secPrice = it.getString(ARG_PARAM_SECPRICE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_moex_sec_item,
                container,
                false
            )

        binding.tvDateVal.text = getFormattedDateString()
        formattedDateLong = formattedDateStringToFormattedDateLong(getFormattedDateString())

        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Выберите дату")
        val materialDatePicker = builder.build()

        materialDatePicker.addOnPositiveButtonClickListener {
            binding.tvDateVal.text = getFormattedDateString(it)
            formattedDateLong = it
        }

        binding.tvDateVal.setOnClickListener {
            materialDatePicker.show(parentFragmentManager, "DATE_PICKER")
        }

        binding.apply {
            textTitle.text =
                textTitle.text.toString()
                    .replace("ХХ", secId!!, true)
            editTextPrice.setText(secPrice!!)

            textViewTotalMoneyIs.text =
                (editTextQuantity.text.toString()
                    .toDouble() * binding.editTextPrice.text.toString()
                    .toDouble()).toString()

            editTextPrice.doAfterTextChanged {
                if (editTextPrice.text.isNotEmpty() && editTextQuantity.text.isNotEmpty())
                    textViewTotalMoneyIs.text = (binding.editTextQuantity.text.toString()
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

                    val newUserPortfolioEntry = UserPortfolioEntry(
                        0,
                        secId!!,
                        editTextPrice.text.toString(),
                        editTextQuantity.text.toString().toInt(),
                        formattedDateLong
                    )
                    viewModel.insert(newUserPortfolioEntry)

                    Toast.makeText(
                        requireContext(),
                        "Добавлено",
                        Toast.LENGTH_SHORT
                    ).show()

                } else
                    Toast.makeText(
                        requireContext(),
                        "Ошибка ввода",
                        Toast.LENGTH_SHORT
                    ).show()

                findNavController().navigateUp()

            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(secId: String?, secPrice: String) =
            AddMoexSecItemFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_SECID, secId)
                    putString(ARG_PARAM_SECPRICE, secPrice)
                }
            }
    }
}