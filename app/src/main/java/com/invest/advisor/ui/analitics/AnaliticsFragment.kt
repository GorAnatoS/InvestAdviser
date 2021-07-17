package com.invest.advisor.ui.analitics

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.invest.advisor.R
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.invest.advisor.data.network.yahooResponse.YahooNetworkDataSourceImpl
import com.invest.advisor.data.network.yahooResponse.YahooApiService
import com.invest.advisor.databinding.FragmentAnaliticsBinding
import com.invest.advisor.ui.base.ScopedFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import java.util.*

/**
 * After click [FragmentAnaliticsBinding.textAnalitics] we have this Fragment with brief info in plots.
 */

class AnaliticsFragment : ScopedFragment(), KodeinAware {
    lateinit var binding: FragmentAnaliticsBinding
    lateinit var viewModel: AnaliticsViewModel

    val pieEntries: MutableList<PieEntry> = ArrayList()
    val updatedSectorEntries: MutableList<PieEntry> = ArrayList()
    var sectorEntries: MutableList<Pair<PieEntry, String>> = ArrayList()

    override val kodein by closestKodein()

    lateinit var mYahooNetworkDataSource: YahooNetworkDataSourceImpl
    lateinit var mYahooApiService: YahooApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_analitics,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(AnaliticsViewModel::class.java)

        mYahooApiService = YahooApiService(ConnectivityInterceptorImpl(requireContext()))
        mYahooNetworkDataSource = YahooNetworkDataSourceImpl(mYahooApiService)

        pieEntries.clear()
        sectorEntries.clear()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.pieChart.setNoDataText("Загружаем...")
        binding.sectorChart.setNoDataText("Загружаем...")

        bindUI()
    }

    private fun setPieChart() {
        val dataSet = PieDataSet(pieEntries, "My Portfolio")

        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        dataSet.valueLinePart1OffsetPercentage = 80f
        dataSet.valueLinePart1Length = 0.2f
        dataSet.valueLinePart2Length = 0.4f

        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        // add a lot of colors
        val colors = ArrayList<Int>()

        for (c in PIE_CHART_COLORS) colors.add(c)

        dataSet.colors = colors

        val data = PieData(dataSet)

        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)

        binding.pieChart.let {
            binding.pieChart.data = data

            //todo
            binding.pieChart.setUsePercentValues(true)
            binding.pieChart.description.isEnabled = false
            binding.pieChart.dragDecelerationFrictionCoef = 0.95f

            binding.pieChart.isDrawHoleEnabled = true
            binding.pieChart.setHoleColor(Color.WHITE)

            binding.pieChart.setTransparentCircleColor(Color.WHITE)
            binding.pieChart.setTransparentCircleAlpha(110)

            binding.pieChart.holeRadius = 58f
            binding.pieChart.transparentCircleRadius = 61f

            binding.pieChart.setDrawCenterText(true)

            binding.pieChart.rotationAngle = 0f
            binding.pieChart.isRotationEnabled = true
            binding.pieChart.isHighlightPerTapEnabled = true

            binding.pieChart.setExtraOffsets(20f, 5f, 20f, 5f)
            binding.pieChart.animateY(1400, Easing.EaseInOutQuad)

            binding.pieChart.legend.isEnabled = false

            // entry label styling
            binding.pieChart.setEntryLabelColor(Color.WHITE)
            binding.pieChart.setEntryLabelTextSize(12f)

            binding.pieChart.centerText = generateCenterSpannableTextForPieChart()

            if (data.entryCount > 0) binding.pieChart.visibility = View.VISIBLE
        }

    }

    private fun setSectorsChart() {

        updatedSectorEntries.clear()
        val newSectorEntrues = sectorEntries.groupBy { it.second }

        for (j in newSectorEntrues.values) {
            var newEntry = PieEntry(
                j.get(0).first.value,
                j.get(0).second
            )

            for (k in j.subList(1, j.size))
                newEntry = PieEntry(
                    newEntry.value + k.first.value,
                    newEntry.label
                )

            updatedSectorEntries.add(newEntry)
        }

        val dataSet = PieDataSet(updatedSectorEntries, "Sector`s portfolio")

        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        dataSet.valueLinePart1OffsetPercentage = 80f
        dataSet.valueLinePart1Length = 0.2f
        dataSet.valueLinePart2Length = 0.4f

        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

        // add a lot of colors
        val colors = ArrayList<Int>()

        for (c in PIE_CHART_COLORS) colors.add(c)

        dataSet.colors = colors

        val data = PieData(dataSet)

        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)

        binding.sectorChart.data = data

        binding.sectorChart.setUsePercentValues(true)
        binding.sectorChart.description.isEnabled = false
        binding.sectorChart.dragDecelerationFrictionCoef = 0.95f

        binding.sectorChart.isDrawHoleEnabled = true
        binding.sectorChart.setHoleColor(Color.WHITE)

        binding.sectorChart.setTransparentCircleColor(Color.WHITE)
        binding.sectorChart.setTransparentCircleAlpha(110)

        binding.sectorChart.holeRadius = 58f
        binding.sectorChart.transparentCircleRadius = 61f

        binding.sectorChart.setDrawCenterText(true)

        binding.sectorChart.rotationAngle = 0f
        // enable rotation of the chart by touch
        binding.sectorChart.isRotationEnabled = true
        binding.sectorChart.isHighlightPerTapEnabled = true

        binding.sectorChart.setExtraOffsets(20f, 5f, 20f, 5f)
        binding.sectorChart.animateY(1400, Easing.EaseInOutQuad)

        binding.sectorChart.legend.isEnabled = false

        // entry label styling
        binding.sectorChart.setEntryLabelColor(Color.BLACK)
        binding.sectorChart.setEntryLabelTextSize(12f)

        binding.sectorChart.centerText = generateCenterSpannableTextForSectors()

        if (data.entryCount > 0) binding.sectorChart.visibility = View.VISIBLE
    }

    fun generateCenterSpannableTextForPieChart(): SpannableString? {
        val pieChartBigTitle = "По компаниям %"
        val totalSum = "Всего: "
        var totalSumMoney = 0.0f

        for (i in pieEntries)
            totalSumMoney += i.value

        val s = SpannableString(
            pieChartBigTitle + "\n"
                    + totalSum
                    + totalSumMoney.toString() + " ₽"
        )
        s.setSpan(RelativeSizeSpan(1.7f), 0, pieChartBigTitle.length, 0)
        s.setSpan(
            StyleSpan(Typeface.NORMAL),
            pieChartBigTitle.length,
            s.length - pieChartBigTitle.length + 1,
            0
        )
        s.setSpan(
            ForegroundColorSpan(Color.GRAY),
            pieChartBigTitle.length,
            s.length - totalSumMoney.toString().length - 2,
            0
        )
        s.setSpan(
            RelativeSizeSpan(.8f),
            pieChartBigTitle.length,
            s.length - totalSumMoney.toString().length - 2,
            0
        )
        s.setSpan(
            StyleSpan(Typeface.ITALIC),
            s.length - totalSumMoney.toString().length - 2,
            s.length,
            0
        )

        s.setSpan(
            ForegroundColorSpan(ColorTemplate.getHoloBlue()),
            s.length - totalSumMoney.toString().length - 2,
            s.length,
            0
        )
        return s
    }

    fun generateCenterSpannableTextForSectors(): SpannableString? {
        val pieChartBigTitle = "По секторам %"
        val totalSum = "Всего: "
        var totalSumMoney = 0.0f

        for (i in pieEntries)
            totalSumMoney += i.value

        val s = SpannableString(
            pieChartBigTitle + "\n"
                    + totalSum
                    + totalSumMoney.toString() + " ₽"
        )
        s.setSpan(RelativeSizeSpan(1.7f), 0, pieChartBigTitle.length, 0)
        s.setSpan(
            StyleSpan(Typeface.NORMAL),
            pieChartBigTitle.length,
            s.length - pieChartBigTitle.length + 1,
            0
        )
        s.setSpan(
            ForegroundColorSpan(Color.GRAY),
            pieChartBigTitle.length,
            s.length - totalSumMoney.toString().length - 2,
            0
        )
        s.setSpan(
            RelativeSizeSpan(.8f),
            pieChartBigTitle.length,
            s.length - totalSumMoney.toString().length - 2,
            0
        )
        s.setSpan(
            StyleSpan(Typeface.ITALIC),
            s.length - totalSumMoney.toString().length - 2,
            s.length,
            0
        )

        s.setSpan(
            ForegroundColorSpan(ColorTemplate.getHoloBlue()),
            s.length - totalSumMoney.toString().length - 2,
            s.length,
            0
        )
        return s
    }

    private fun bindUI() = launch {

        mYahooNetworkDataSource.downloadedYahooResponse.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            val symb = it.quoteSummary.result[0].price.symbol.substring(
                        0,
                        it.quoteSummary.result[0].price.symbol.length - 3
                    ).uppercase(Locale.getDefault())

            var index = -1
            var i = 0
            for (se in sectorEntries) {
                if (se.second == symb) {
                    index = i
                    break
                }
                i++
            }

            if (index != -1) {
                sectorEntries.set(
                    index,
                    Pair(
                        sectorEntries[index].first,
                        if (it.quoteSummary.result[0].assetProfile.sector.isNullOrEmpty()) "Other"
                        else it.quoteSummary.result[0].assetProfile.sector
                    )
                )

                binding.textAnalitics.text =
                    binding.textAnalitics.text.toString() + symb + " " + sectorEntries[index].first.toString() + " " + sectorEntries[index].second + '\n'//it.quoteSummary.result[0].price?.shortName + " " + it.quoteSummary.result[0].assetProfile?.industry + "  " + it.quoteSummary.result[0].assetProfile?.sector + " " + it.quoteSummary.result[0].financialData?.currentPrice?.fmt + '\n'
            }

            setSectorsChart()
        })

        //get data from Room
        viewModel.allData.observe(viewLifecycleOwner, Observer {
            GlobalScope.launch(Dispatchers.IO) {

                //суммирование одинаковых тикеров в портфеке
                val myPortfolioItemList = it.groupBy { it.secId }

                for (j in myPortfolioItemList.values) {
                    var newEntry = PieEntry(
                        j.get(0).secPrice.toFloat() * j.get(0).secQuantity.toFloat(), j.get(
                            0
                        ).secId
                    )

                    for (k in j.subList(1, j.size))
                        newEntry = PieEntry(
                            newEntry.value + k.secPrice.toFloat() * k.secQuantity.toFloat(),
                            newEntry.label
                        )

                    pieEntries.add(newEntry)
                    sectorEntries.add(Pair(newEntry, newEntry.label))

                    mYahooNetworkDataSource.fetchYahooData(newEntry.label + ".ME")

                }

                GlobalScope.launch(Dispatchers.Main) {
                    //sorting 9 most expensive shares
                    var sortedPieEntries =
                        pieEntries.toList().sortedByDescending { it.value }.toMutableList()

                    pieEntries.clear()

                    for ((i, pe) in sortedPieEntries.withIndex()) {
                        when {
                            i < CONST_MAX_PIECHART_VALUES -> pieEntries.add(pe)
                            i == CONST_MAX_PIECHART_VALUES -> pieEntries.add(
                                PieEntry(
                                    pe.value,
                                    "Другие"
                                )
                            )
                            i > CONST_MAX_PIECHART_VALUES -> {
                                pieEntries[CONST_MAX_PIECHART_VALUES] = PieEntry(
                                    pieEntries[CONST_MAX_PIECHART_VALUES].value + pe.value,
                                    "Другие"
                                )
                            }
                        }
                    }

                    setPieChart()
                }
            }
        })
    }
}

// TODO: 10/20/2020 Colors
// TODO: 10/20/2020 сделать скажем 10 цветов, 9 основных, а 1 для всего другого. Так и отображать

const val CONST_MAX_PIECHART_VALUES = 9
val PIE_CHART_COLORS = intArrayOf(
    Color.rgb(38, 70, 83),
    Color.rgb(42, 157, 143),
    Color.rgb(233, 196, 106),
    Color.rgb(244, 162, 97),
    Color.rgb(231, 111, 81),
    Color.rgb(109, 69, 76),
    Color.rgb(144, 45, 65),
    Color.rgb(120, 88, 111),
    Color.rgb(255, 146, 139),
    Color.rgb(187, 153, 156)
)

// TODO: 10/21/2020 !! Сделать ViewModel, т.к. удаляет если меняюь фрагмент 