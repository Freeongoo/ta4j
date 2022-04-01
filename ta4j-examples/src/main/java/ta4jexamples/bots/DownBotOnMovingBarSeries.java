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
package ta4jexamples.bots;

import org.ta4j.core.*;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import ta4jexamples.loaders.CustomCsvBarsLoader;
import ta4jexamples.strategies.DownStrategy;
import ta4jexamples.utils.DisplayStatsUtils;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * This class is an example of a dummy trading bot using ta4j.
 * <p/>
 */
public class DownBotOnMovingBarSeries {

    public static void main(String[] args) {
        BarSeries seriesFromFile = CustomCsvBarsLoader.loadCsvSeries("BTC", "20211108-20220124_BTC-USDT_min5.csv", "yyyy-MM-dd'T'HH:mm:ss");

        BarSeries series = new BaseBarSeries();

        Strategy strategy;

        TradingRecord tradingRecord = new BaseTradingRecord();

        List<Bar> barData = seriesFromFile.getBarData();
        for (Bar bar : barData) {
            ZonedDateTime endTime = bar.getEndTime();
            Num closePrice = bar.getClosePrice();
            Num openPrice = bar.getOpenPrice();
            Num highPrice = bar.getHighPrice();
            Num lowPrice = bar.getLowPrice();
            Num volume = bar.getVolume();

            List<Num> listOfPriceFromBar = List.of(openPrice, highPrice, lowPrice, closePrice);

            Bar newBar = new BaseBar(Duration.ofSeconds(300), endTime, series.function());
            boolean isReplace = false;
            for (Num incomePrice : listOfPriceFromBar) {
                newBar.addTrade(volume, incomePrice);
                series.addBar(newBar, isReplace);
                strategy = DownStrategy.buildStrategy("down", series, DecimalNum.valueOf(0.9), 5);
                Bar lastBar = series.getLastBar();

                int endIndex = series.getEndIndex();
                if (strategy.shouldEnter(endIndex)) {
                    // Our strategy should enter
                    boolean entered = tradingRecord.enter(endIndex, lastBar.getClosePrice(), DecimalNum.valueOf(10));
                    if (entered) {
                        Trade entry = tradingRecord.getLastEntry();
                        System.out.println("Entered on " + entry.getIndex() + " (price=" + entry.getNetPrice().doubleValue()
                                + ", amount=" + entry.getAmount().doubleValue() + ")");
                    }
                } else if (strategy.shouldExit(endIndex, tradingRecord)) {
                    // Our strategy should exit
                    System.out.println("Strategy should EXIT on " + endIndex);
                    boolean exited = tradingRecord.exit(endIndex, lastBar.getClosePrice(), DecimalNum.valueOf(10));
                    if (exited) {
                        Trade exit = tradingRecord.getLastExit();
                        System.out.println("Exited on " + exit.getIndex() + " (price=" + exit.getNetPrice().doubleValue()
                                + ", amount=" + exit.getAmount().doubleValue() + ")");
                    }
                }

                isReplace = true;
            }
        }

        DisplayStatsUtils.printStat(series, tradingRecord);
    }
}
