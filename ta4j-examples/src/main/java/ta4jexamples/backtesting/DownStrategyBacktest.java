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
package ta4jexamples.backtesting;

import org.ta4j.core.BacktestExecutor;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;
import org.ta4j.core.Trade;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.reports.TradingStatement;
import ta4jexamples.loaders.CustomCsvBarsLoader;
import ta4jexamples.strategies.DownStrategy;
import ta4jexamples.utils.DisplayStatsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

public class DownStrategyBacktest {

    public static void main(String[] args) {
        //BarSeries series = CustomCsvBarsLoader.loadCsvSeries("BTC", "20210330-20220330_BTC-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");
        //BarSeries series = CustomCsvBarsLoader.loadCsvSeries("BTC", "20211108-20220124_BTC-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");
        //BarSeries series = CustomCsvBarsLoader.loadCsvSeries("ETH", "20210330-20220330_ETH-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");
        BarSeries series = CustomCsvBarsLoader.loadCsvSeries("LTC", "20210330-20220330_LTC-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");

        final List<Strategy> strategies = new ArrayList<>();
        for (double i = 0.5; i <= 2.; i = i + 0.1) {
            for (int j = 3; j <= 20; j = j + 2) {
                Strategy strategy = DownStrategy.buildStrategy("Down percent:" + i + " count: " + j, series, DecimalNum.valueOf(i), j);
                strategies.add(strategy);
            }
        }
        BacktestExecutor backtestExecutor = new BacktestExecutor(series);
        List<TradingStatement> execute = backtestExecutor.execute(strategies, DecimalNum.valueOf(50), Trade.TradeType.BUY).stream()
                .sorted(comparing(e -> e.getPerformanceReport().getTotalProfitLossPercentage(), reverseOrder()))
                .collect(Collectors.toList());
        DisplayStatsUtils.printStats(execute);

        // -- best
        /*BTC
        Name of strategy: Down percent:0.7999999999999999 count: 13
        totalProfitLoss: 3068325.0
        totalProfitLossPercentage: 154.84338513596575155049000446189

        ETH
        Name of strategy: Down percent:1.7000000000000004 count: 5
        totalProfitLoss: 116159.50
        totalProfitLossPercentage: 105.77643745844940642241715296237

        LTC
        Name of strategy: Down percent:1.2 count: 7
        totalProfitLoss: 24119.700
        totalProfitLossPercentage: 296.61456230275236339449998983626
        */

        // --- optimal
        /*BTC
        Name of strategy: Down percent:0.7999999999999999 count: 13
        totalProfitLoss: 3068325.0
        totalProfitLossPercentage: 154.84338513596575155049000446189

        ETH
        Name of strategy: Down percent:0.7999999999999999 count: 13
        totalProfitLoss: 106557.50
        totalProfitLossPercentage: 90.372392592095446695916454708726

        LTC
        Name of strategy: Down percent:0.7999999999999999 count: 13
        totalProfitLoss: 14316.200
        totalProfitLossPercentage: 218.35318665714887898859816969626*/
    }
}
