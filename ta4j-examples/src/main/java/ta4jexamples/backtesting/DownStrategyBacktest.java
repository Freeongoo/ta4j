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
        BarSeries series = CustomCsvBarsLoader.loadCsvSeries("BTC", "20211108-20220124_BTC-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");

        final List<Strategy> strategies = new ArrayList<>();
        for (double i = 0.5; i <= 3.; i = i + 0.1) {
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

        // *******************************************
        // *** 20210330-20220330_BTC-USDT_min5.csv ***
        // *******************************************
        /*Name of strategy: Down percent:0.8999999999999999 count: 5
        totalProfitLoss: 2662185.0
        totalProfitLossPercentage: 134.63170704884587301851158010020
        totalLoss: -19213325.0
        totalProfit: 21875510.0
        lossCount: 468
        profitCount: 788

        Name of strategy: Down percent:0.7999999999999999 count: 11
        totalProfitLoss: 2042130.0
        totalProfitLossPercentage: 118.91269424012929408732693246936
        totalLoss: -27253805.0
        totalProfit: 29295935.0
        lossCount: 664
        profitCount: 1042

        Name of strategy: Down percent:0.7999999999999999 count: 5
        totalProfitLoss: 2280240.0
        totalProfitLossPercentage: 118.26843983923454441147861142831
        totalLoss: -22437100.0
        totalProfit: 24717340.0
        lossCount: 550
        profitCount: 885

        Name of strategy: Down percent:0.7999999999999999 count: 13
        totalProfitLoss: 2060400.0
        totalProfitLossPercentage: 117.32564568638210047625074676990
        totalLoss: -27932805.0
        totalProfit: 29993205.0
        lossCount: 682
        profitCount: 1061*/

        // *******************************************
        // *** 20211108-20220124_BTC-USDT_min5.csv ***
        // *******************************************
        /*Name of strategy: Down percent:2.600000000000001 count: 7
        totalProfitLoss: 73900.0
        totalProfitLossPercentage: 4.0507822445485920316631310582930
        totalLoss: -533760.0
        totalProfit: 607660.0
        lossCount: 6
        profitCount: 17

        Name of strategy: Down percent:1.6000000000000003 count: 9
        totalProfitLoss: 8220.0
        totalProfitLossPercentage: 1.8231257265917115980459167441622
        totalLoss: -1722460.0
        totalProfit: 1730680.0
        lossCount: 32
        profitCount: 55

        Name of strategy: Down percent:2.0000000000000004 count: 3
        totalProfitLoss: 11485.0
        totalProfitLossPercentage: 1.4581604774120629949824820607138
        totalLoss: -490505.0
        totalProfit: 501990.0
        lossCount: 5
        profitCount: 14

        Name of strategy: Down percent:2.1000000000000005 count: 3
        totalProfitLoss: -4835.0
        totalProfitLossPercentage: 0.7890475696107004312347557339785
        totalLoss: -455905.0
        totalProfit: 451070.0
        lossCount: 4
        profitCount: 12

        Name of strategy: Down percent:0.8999999999999999 count: 5
        totalProfitLoss: -75110.0
        totalProfitLossPercentage: 0.3790469975280197058218029817740
        totalLoss: -3651590.0
        totalProfit: 3576480.0
        lossCount: 76
        profitCount: 117*/
    }
}
