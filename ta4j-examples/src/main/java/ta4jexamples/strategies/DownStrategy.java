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
import ta4jexamples.utils.DisplayStatsUtils;
import ta4jexamples.utils.MathUtils;
import ta4jexamples.utils.PriceUtils;

public class DownStrategy {

    public static Strategy buildStrategy(String name, BarSeries series, Num gainPercentage, int barCount) {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }

        return new BaseStrategy(name, createEntryRule(series, gainPercentage, barCount),
                createExitRule(series, barCount));
    }

    private static Rule createEntryRule(BarSeries series, Num gainPercentage, int barCount) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        return new PercentDownRule(closePrice, gainPercentage, barCount);
    }

    private static Rule createExitRule(BarSeries series, int barCount) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        return new StopLossRule(closePrice, DecimalNum.valueOf(1.5))
                .or(new StopGainRule(closePrice, DecimalNum.valueOf(1.)));
    }

    public static void main(String[] args) {
        // Getting the bar series
        //BarSeries series = CsvTradesLoader.loadBitstampSeries();
        //BarSeries series = CsvBarsLoader.loadCsvSeries("BTC", "20211108-20220124_BTC-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");
        BarSeries series = CsvBarsLoader.loadCsvSeries("BTC", "20210330-20220330_BTC-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");

        // Building the trading strategy
        //Strategy strategy = buildStrategy("down", series, DecimalNum.valueOf(0.9), 5);
        Strategy strategy = buildStrategy("down", series, DecimalNum.valueOf(1.3), 7);

        // Running the strategy
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
        DisplayStatsUtils.printStat(series, tradingRecord);

        // Total return: 2.7650859409464753441105102817617  0.9 5
        // Total return: 2.8732216648275912873136885284400  1.3 7
    }
}
