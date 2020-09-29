package com.invest.advisor.ui.moex.detailedMoexItem

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.invest.advisor.R
import com.invest.advisor.data.db.userPortfolio.UserPortfolioEntry
import com.invest.advisor.ui.base.ScopedFragment
import com.invest.advisor.ui.portfolio.PortfolioViewModel
import kotlinx.android.synthetic.main.fragment_moex_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*

class DetailedMoexItemFragment : ScopedFragment() {//, KodeinAware{

    private lateinit var viewModel: PortfolioViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_moex_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Toast.makeText(requireContext(), arguments?.getString("secId")!!, Toast.LENGTH_LONG).show()

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
            } else    Toast.makeText(
                requireContext(),
                "Ошибка ввода",
                Toast.LENGTH_LONG
            ).show()


            findNavController().navigateUp()
        }

    }
}