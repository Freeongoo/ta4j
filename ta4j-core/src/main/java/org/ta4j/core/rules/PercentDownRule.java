package org.ta4j.core.rules;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * @author dorofeev
 */
public class PercentDownRule extends AbstractRule {

    /**
     * The close price indicator
     */
    private final ClosePriceIndicator closePrice;

    private final int numberCandlesBeforeForCheck;

    /**
     * The gain percentage
     */
    private final Num gainPercentage;

    public PercentDownRule(ClosePriceIndicator closePrice, Num gainPercentage, int numberCandlesBeforeForCheck) {
        this.closePrice = closePrice;
        this.gainPercentage = gainPercentage;
        this.numberCandlesBeforeForCheck = numberCandlesBeforeForCheck;
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (index <= numberCandlesBeforeForCheck) {
            return false;
        }
        Num current = closePrice.getValue(index);
        BarSeries barSeries = closePrice.getBarSeries();
        int indexTo = index - numberCandlesBeforeForCheck;
        List<Bar> bars = new ArrayList<>();
        for (int i = indexTo; i <= index; i++) {
            bars.add(barSeries.getBar(i));
        }

        double maxMinValue = bars.stream()
                .mapToDouble(b -> b.getClosePrice().doubleValue())
                .max().orElseThrow(() -> new RuntimeException(""));

        if (current.doubleValue() > maxMinValue) {
            return false;
        }

        Double percentDiff = getPercentDiff(current.doubleValue(), maxMinValue);
        boolean isTrue = percentDiff > gainPercentage.doubleValue();
        //System.out.println(isTrue);
        return isTrue;
    }

    public static <V extends Number> Double getPercentDiff(V value1, V value2) {
        if (isNull(value1) || isNull(value2)) {
            return null;
        }

        if (value1.doubleValue() == value2.doubleValue()) {
            return 0.;
        }

        Double percent;
        if (value1.doubleValue() > value2.doubleValue()) {
            percent = calcPercent(value1, value2);
        } else {
            percent = calcPercent(value2, value1);
        }

        if (isNull(percent)) {
            return null;
        }

        return 100. - percent;
    }

    public static <V extends Number> Double calcPercent(V valueBase, V valuePart) {
        if (isNull(valueBase) || valueBase.doubleValue() == 0. || isNull(valuePart)) {
            return null;
        }

        return (valuePart.doubleValue() * 100.) / valueBase.doubleValue();
    }
}
