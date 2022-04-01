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
import ta4jexamples.strategies.GlobalExtremaStrategy;
import ta4jexamples.utils.DisplayStatsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

public class GlobalExtremaBacktestWithShowStatsBtc5Min {

    public static void main(String[] args) {
        BarSeries series = CustomCsvBarsLoader.loadCsvSeries("BTC", "20211108-20220124_BTC-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");

        final List<Strategy> strategies = new ArrayList<>();
        /*for (int i = 5; i <= 11; i ++) {
            for (int j = i + 10; j <= 80; j = j + 10) {
                Strategy strategy = RSI2Strategy.buildStrategy("RSI " + i + "_" + j, series, i, j, 2);
                strategies.add(strategy);
            }
        }*/
        Strategy strategy = GlobalExtremaStrategy.buildStrategy(series);
        strategies.add(strategy);

        BacktestExecutor backtestExecutor = new BacktestExecutor(series);
        List<TradingStatement> execute = backtestExecutor.execute(strategies, DecimalNum.valueOf(50), Trade.TradeType.BUY).stream()
                .sorted(comparing(e -> e.getPerformanceReport().getTotalProfitLossPercentage(), reverseOrder()))
                .collect(Collectors.toList());
        DisplayStatsUtils.printStats(execute);
    }
}
