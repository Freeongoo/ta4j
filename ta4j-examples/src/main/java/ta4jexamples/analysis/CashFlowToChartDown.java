/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan, 2017-2021 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ta4jexamples.analysis;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.ta4j.core.*;
import org.ta4j.core.analysis.CashFlow;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import ta4jexamples.loaders.CsvBarsLoader;
import ta4jexamples.loaders.CsvTradesLoader;
import ta4jexamples.loaders.CustomCsvBarsLoader;
import ta4jexamples.strategies.DownStrategy;
import ta4jexamples.strategies.MovingMomentumStrategy;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class builds a graphical chart showing the cash flow of a strategy.
 */
public class CashFlowToChartDown {

    /**
     * Builds a JFreeChart time series from a Ta4j bar series and an indicator.
     *
     * @param barSeries the ta4j bar series
     * @param indicator the indicator
     * @param name      the name of the chart time series
     * @return the JFreeChart time series
     */
    private static org.jfree.data.time.TimeSeries buildChartBarSeries(BarSeries barSeries, Indicator<Num> indicator,
            String name) {
        org.jfree.data.time.TimeSeries chartBarSeries = new org.jfree.data.time.TimeSeries(name);
        for (int i = 0; i < barSeries.getBarCount(); i++) {
            Bar bar = barSeries.getBar(i);
            chartBarSeries.add(new Minute(new Date(bar.getEndTime().toEpochSecond() * 1000)),
                    indicator.getValue(i).doubleValue());
        }
        return chartBarSeries;
    }

    /**
     * Adds the cash flow axis to the plot.
     *
     * @param plot    the plot
     * @param dataset the cash flow dataset
     */
    private static void addCashFlowAxis(XYPlot plot, TimeSeriesCollection dataset) {
        final NumberAxis cashAxis = new NumberAxis("Cash Flow Ratio");
        cashAxis.setAutoRangeIncludesZero(false);
        plot.setRangeAxis(1, cashAxis);
        plot.setDataset(1, dataset);
        plot.mapDatasetToRangeAxis(1, 1);
        final StandardXYItemRenderer cashFlowRenderer = new StandardXYItemRenderer();
        cashFlowRenderer.setSeriesPaint(0, Color.blue);
        plot.setRenderer(1, cashFlowRenderer);
    }

    /**
     * Displays a chart in a frame.
     *
     * @param chart the chart to be displayed
     */
    private static void displayChart(JFreeChart chart) {
        // Chart panel
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(1024, 400));
        // Application frame
        ApplicationFrame frame = new ApplicationFrame("Ta4j example - Cash flow to chart");
        frame.setContentPane(panel);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        // Getting the bar series
        //BarSeries series = CsvTradesLoader.loadBitstampSeries();
        //BarSeries series = CustomCsvBarsLoader.loadCsvSeries("BTC", "20211108-20220330_BTC-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");
        //BarSeries series = CustomCsvBarsLoader.loadCsvSeries("BTC", "20210330-20220330_BTC-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");
        //BarSeries series = CustomCsvBarsLoader.loadCsvSeries("ETH", "20210330-20220330_ETH-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");
        BarSeries series = CustomCsvBarsLoader.loadCsvSeries("LTC", "20210330-20220330_LTC-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");

        // Building the trading strategy
        Strategy strategy = DownStrategy.buildStrategy("down", series, DecimalNum.valueOf(0.8), 13);
        //Strategy strategy = DownStrategy.buildStrategy("Down percent:" + 1.3 + " count: " + 7, series, DecimalNum.valueOf(1.3), 7);

        // Running the strategy
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
        // Getting the cash flow of the resulting positions
        CashFlow cashFlow = new CashFlow(series, tradingRecord);

        /*
         * Building chart datasets
         */
        TimeSeriesCollection datasetAxis1 = new TimeSeriesCollection();
        datasetAxis1.addSeries(buildChartBarSeries(series, new ClosePriceIndicator(series), "Bitstamp Bitcoin (BTC)"));
        TimeSeriesCollection datasetAxis2 = new TimeSeriesCollection();
        datasetAxis2.addSeries(buildChartBarSeries(series, cashFlow, "Cash Flow"));

        /*
         * Creating the chart
         */
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Bitstamp BTC", // title
                "Date", // x-axis label
                "Price", // y-axis label
                datasetAxis1, // data
                true, // create legend?
                true, // generate tooltips?
                false // generate URLs?
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MM-dd HH:mm"));

        /*
         * Adding the cash flow axis (on the right)
         */
        addCashFlowAxis(plot, datasetAxis2);

        /*
         * Displaying the chart
         */
        displayChart(chart);
    }
}
