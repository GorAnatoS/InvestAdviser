package com.invest.advisor.ui.detailedMoexItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.invest.advisor.R
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioEntry
import com.invest.advisor.databinding.FragmentAddShareBinding
import com.invest.advisor.internal.DateHelper.Companion.formattedDateStringToFormattedDateLong
import com.invest.advisor.internal.DateHelper.Companion.getFormattedDateString
import com.invest.advisor.ui.portfolio.PortfolioViewModel

private const val ARG_PARAM1 = "secId"
private const val ARG_PARAM2 = "secPrice"

class DetailedMoexItemFragment : Fragment() {
    private lateinit var rootView: FragmentAddShareBinding
    private lateinit var viewModel: PortfolioViewModel

    private var secId: String? = null
    private var secPrice: String? = null

    private var formattedDateLong: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =
            ViewModelProvider(requireActivity()).get(PortfolioViewModel::class.java)

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
                R.layout.fragment_add_share,
                container,
                false
            )

        rootView.tvDateVal.text = getFormattedDateString()
        formattedDateLong = formattedDateStringToFormattedDateLong(getFormattedDateString())

        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Выберите дату")
        val materialDatePicker = builder.build()

        materialDatePicker.addOnPositiveButtonClickListener {
            rootView.tvDateVal.text = getFormattedDateString(it)
            formattedDateLong = it
        }

        rootView.tvDateVal.setOnClickListener {
            materialDatePicker.show(parentFragmentManager, "DATE_PICKER")
        }

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
        return rootView.root
    }

    companion object {
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