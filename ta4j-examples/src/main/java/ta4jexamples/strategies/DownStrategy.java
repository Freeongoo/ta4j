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
package ta4jexamples.strategies;

import org.ta4j.core.*;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.*;
import ta4jexamples.loaders.CsvBarsLoader;
import ta4jexamples.loaders.CustomCsvBarsLoader;
import ta4jexamples.utils.DisplayStatsUtils;
import ta4jexamples.utils.MathUtils;
import ta4jexamples.utils.PriceUtils;

public class DownStrategy {

    public static Strategy buildStrategy(String name, BarSeries series, Num gainPercentage, int barCount) {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }

        return new BaseStrategy(name, createEntryRule(series, gainPercentage, barCount),
                createExitRule(series));
    }

    private static Rule createEntryRule(BarSeries series, Num gainPercentage, int barCount) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        return new PercentDownRule(closePrice, gainPercentage, barCount);
    }

    private static Rule createExitRule(BarSeries series) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        return new StopLossRule(closePrice, DecimalNum.valueOf(1.5))
                .or(new StopGainRule(closePrice, DecimalNum.valueOf(1.)));
    }

    public static void main(String[] args) {
        // Getting the bar series
        //BarSeries series = CsvTradesLoader.loadBitstampSeries();
        //BarSeries series = CustomCsvBarsLoader.loadCsvSeries("BTC", "20211108-20220124_BTC-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");
        //BarSeries series = CustomCsvBarsLoader.loadCsvSeries("BTC", "20210330-20220330_BTC-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");
        BarSeries series = CustomCsvBarsLoader.loadCsvSeries("ETH", "20210330-20220330_ETH-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");

        // Building the trading strategy
        Strategy strategy = buildStrategy("down", series, DecimalNum.valueOf(0.8), 13);
        //Strategy strategy = buildStrategy("down", series, DecimalNum.valueOf(1.3), 7);

        // Running the strategy
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
        DisplayStatsUtils.printStat(series, tradingRecord);

        // ** "20211108-20220124_BTC-USDT_min5.csv" **
        // Total return: 0.97586817146461803767387509311276  0.9 5
        // Total return: 0.86578267057319301799547158661506  1.3 7

        // ** "20210330-20220330_BTC-USDT_min5.csv" **
        // Total return: 3.2276338288860000007573992975957  0.9 5
        // Total return: 1.5518963290498996920900377520147  1.3 7
    }
}
