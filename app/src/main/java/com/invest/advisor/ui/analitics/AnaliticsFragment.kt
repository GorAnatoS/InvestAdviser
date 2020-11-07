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
import com.invest.advisor.data.network.YahooNetworkDataSourceImpl
import com.invest.advisor.data.network.yahooResponse.YahooApiService
import com.invest.advisor.ui.base.ScopedFragment
import com.invest.advisor.ui.portfolio.PortfolioViewModel
import kotlinx.android.synthetic.main.fragment_analitics.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import java.util.*

class AnaliticsFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    lateinit var mYahooNetworkDataSource: YahooNetworkDataSourceImpl
    lateinit var mYahooApiService: YahooApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        portfolioViewModel = ViewModelProvider(this).get(PortfolioViewModel::class.java)

        val mYahooApiService = YahooApiService(ConnectivityInterceptorImpl(requireContext()))
        mYahooNetworkDataSource = YahooNetworkDataSourceImpl(mYahooApiService)

        pieEntries.clear()
        sectorEntries.clear()



        return inflater.inflate(R.layout.fragment_analitics, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pieChart.setNoDataText("Загружаем...")
        sectorChart.setNoDataText("Загружаем...")

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

        //dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        //end new

        // add a lot of colors
        val colors = ArrayList<Int>()

        for (c in PIE_CHART_COLORS) colors.add(c)
        //for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        //for (c in ColorTemplate.PASTEL_COLORS) colors.add(c) +
        //colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors

        val data = PieData(dataSet)

        /*data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
*/
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)

        pieChart?.let {
            pieChart.data = data

            //todo
            pieChart.setUsePercentValues(true)
            pieChart.description.isEnabled = false
            pieChart.dragDecelerationFrictionCoef = 0.95f

            pieChart.isDrawHoleEnabled = true
            pieChart.setHoleColor(Color.WHITE)

            pieChart.setTransparentCircleColor(Color.WHITE)
            pieChart.setTransparentCircleAlpha(110)

            pieChart.holeRadius = 58f
            pieChart.transparentCircleRadius = 61f

            pieChart.setDrawCenterText(true)

            pieChart.rotationAngle = 0f
            // enable rotation of the chart by touch
            // enable rotation of the chart by touch
            pieChart.isRotationEnabled = true
            pieChart.isHighlightPerTapEnabled = true

            // chart.setUnit(" €");
            //chart.setExtraOffsets(5f, 10f, 5f, 5f)
            pieChart.setExtraOffsets(20f, 5f, 20f, 5f)
            pieChart.animateY(1400, Easing.EaseInOutQuad)

            // chart.spin(2000, 0, 360);
/*        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f*/

            pieChart.legend.isEnabled = false

            // entry label styling
            pieChart.setEntryLabelColor(Color.WHITE)
            //chart.setEntryLabelTypeface(tfRegular)
            pieChart.setEntryLabelTextSize(12f)

            pieChart.centerText = generateCenterSpannableTextForPieChart()}

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

        //end new

        // add a lot of colors
        val colors = ArrayList<Int>()

        for (c in PIE_CHART_COLORS) colors.add(c)
        //for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        //for (c in ColorTemplate.PASTEL_COLORS) colors.add(c) +
        //colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors

        val data = PieData(dataSet)

        /*data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
*/
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)

        sectorChart.data = data

        //todo
        sectorChart.setUsePercentValues(true)
        sectorChart.description.isEnabled = false
        sectorChart.dragDecelerationFrictionCoef = 0.95f

        sectorChart.isDrawHoleEnabled = true
        sectorChart.setHoleColor(Color.WHITE)

        sectorChart.setTransparentCircleColor(Color.WHITE)
        sectorChart.setTransparentCircleAlpha(110)

        sectorChart.holeRadius = 58f
        sectorChart.transparentCircleRadius = 61f

        sectorChart.setDrawCenterText(true)

        sectorChart.rotationAngle = 0f
        // enable rotation of the chart by touch
        // enable rotation of the chart by touch
        sectorChart.isRotationEnabled = true
        sectorChart.isHighlightPerTapEnabled = true

        //chart.setUnit(" €");
        //chart.setExtraOffsets(5f, 10f, 5f, 5f)
        sectorChart.setExtraOffsets(20f, 5f, 20f, 5f)
        sectorChart.animateY(1400, Easing.EaseInOutQuad)

        // chart.spin(2000, 0, 360);
/*        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f*/

        sectorChart.legend.isEnabled = false

        // entry label styling
        sectorChart.setEntryLabelColor(Color.BLACK)
        //chart.setEntryLabelTypeface(tfRegular)
        sectorChart.setEntryLabelTextSize(12f)

        sectorChart.centerText = generateCenterSpannableTextForSectors()
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

            val symb = it.quoteSummary.result[0].price?.symbol.substring(
                0,
                it.quoteSummary.result[0].price?.symbol.length - 3
            ).toUpperCase()

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
                        if (it.quoteSummary.result[0].assetProfile?.sector.isNullOrEmpty()) "Other"
                        else it.quoteSummary.result[0].assetProfile?.sector
                    )
                )

                text_analitics.text =
                    text_analitics.text.toString() + symb + " " + sectorEntries[index].first.toString() + " " + sectorEntries[index].second + '\n'//it.quoteSummary.result[0].price?.shortName + " " + it.quoteSummary.result[0].assetProfile?.industry + "  " + it.quoteSummary.result[0].assetProfile?.sector + " " + it.quoteSummary.result[0].financialData?.currentPrice?.fmt + '\n'
            }

            setSectorsChart()


        })

        //get data from Room
        portfolioViewModel.allData.observe(viewLifecycleOwner, Observer {
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

    companion object {
        private lateinit var portfolioViewModel: PortfolioViewModel
        val pieEntries: MutableList<PieEntry> = ArrayList()
        val updatedSectorEntries: MutableList<PieEntry> = ArrayList()
        var sectorEntries: MutableList<Pair<PieEntry, String>> = ArrayList()
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