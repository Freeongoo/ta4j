package ta4jexamples.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

import static java.util.Objects.isNull;

public class MathUtils {

    /**
     * @param stageId stageId
     * @return percent
     */
    public static double calculatePercentByStageIdDefault(Long stageId) {
        if (stageId == 0L) {
            return 0.;
        }
        double result = stageId * 2. * 12;
        return Math.min(result, 88.);
    }

    public static double calculatePercentByStageId2(Long stageId) {
        if (stageId == 0L) {
            return 0.;
        }
        double result = stageId * 10;
        return Math.min(result, 88.);
    }

    public static double calculatePercentByStageId3(Long stageId) {
        if (stageId == 0L) {
            return 0.;
        }
        double result = stageId * 15;
        return Math.min(result, 88.);
    }

    public static <V extends Number> double arithmeticalMean(Collection<V> values) {
        int size = values.size();
        if (size == 0) {
            return 0.;
        }
        double sum = values.stream().mapToDouble(v -> v.doubleValue()).sum();
        return sum / size;
    }

    public static <V extends Number> double arithmeticalMean(V ...values) {
        int size = values.length;
        if (size == 0) {
            return 0.;
        }
        double sum = Arrays.stream(values).mapToDouble(v -> v.doubleValue()).sum();
        return sum / size;
    }

    /**
     * Ex.: +23.1 or -42.12
     *
     * @param isPositive isPositive
     * @param number number
     * @param <V> V
     * @return string of number with sign
     */
    public static <V extends Number> String getNumberWithSign(boolean isPositive, V number) {
        String digitsWithoutSign = number.toString().replaceAll("[^0-9.]", "");
        String sing = isPositive ? "+" : "-";
        return sing + digitsWithoutSign;
    }

    public static <V extends Number> V getMin(V value1, V value2) {
        if (value1.doubleValue() > value2.doubleValue()) {
            return value2;
        } else {
            return value1;
        }
    }

    public static <V extends Number> V getMax(V value1, V value2) {
        if (value1.doubleValue() < value2.doubleValue()) {
            return value2;
        } else {
            return value1;
        }
    }

    /**
     * @param valueBase valueBase
     * @param valuePart valuePart
     * @param <V> V
     * @return percent
     */
    public static <V extends Number> Double calcPercent(V valueBase, V valuePart) {
        if (isNull(valueBase) || valueBase.doubleValue() == 0. || isNull(valuePart)) {
            return null;
        }

        return (valuePart.doubleValue() * 100.) / valueBase.doubleValue();
    }

    public static BigDecimal calcPercent(BigDecimal valueBase, BigDecimal valuePart) {
        if (isNull(valueBase) || valueBase.compareTo(new BigDecimal("0")) == 0 || isNull(valuePart)) {
            return null;
        }

        return valuePart.multiply(new BigDecimal("100")).divide(valueBase, MathContext.DECIMAL32);
    }

    public static <V extends Number> Double calcByPassedPercent(V valueBase, V percent) {
        if (isNull(valueBase) || valueBase.doubleValue() == 0. || isNull(percent)) {
            return null;
        }

        return (valueBase.doubleValue() * percent.doubleValue()) / 100.;
    }

    public static <V extends Number> Double increaseByPercent(V valueBase, V percent) {
        if (isNull(valueBase)) {
            return null;
        }

        if (isNull(percent) || percent.doubleValue() == 0.) {
            return valueBase.doubleValue();
        }

        double value = (valueBase.doubleValue() * percent.doubleValue()) / 100.;
        return valueBase.doubleValue() + value;
    }

    public static <V extends Number> Double decreaseByPercent(V valueBase, V percent) {
        if (isNull(valueBase)) {
            return null;
        }

        if (isNull(percent) || percent.doubleValue() == 0.) {
            return valueBase.doubleValue();
        }

        double value = (valueBase.doubleValue() * percent.doubleValue()) / 100.;
        return valueBase.doubleValue() - value;
    }

    /**
     * @param value1 value1
     * @param value2 value2
     * @param <V> V
     * @return percent diff
     */
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

    public static BigDecimal getPercentDiffBig(BigDecimal value1, BigDecimal value2) {
        if (isNull(value1) || isNull(value2)) {
            return null;
        }

        if (value1.compareTo(value2) == 0) {
            return new BigDecimal("0");
        }

        BigDecimal percent;
        if (value1.compareTo(value2) > 0) {
            percent = calcPercent(value1, value2);
        } else {
            percent = calcPercent(value2, value1);
        }

        if (isNull(percent)) {
            return null;
        }

        return new BigDecimal("100").subtract(percent);
    }

    public static double round(Double value) {
        return round(value, 2);
    }

    public static double round(Double value, int scale) {
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
    }

    public static double roundCeiling(Double value, int scale) {
        return new BigDecimal(value).setScale(scale, RoundingMode.CEILING).doubleValue();
    }

    public static double roundNullable(Double value) {
        if (isNull(value)) {
            return 0.;
        }
        return round(value, 2);
    }

    public static double roundNullable(Double value, int scale) {
        if (isNull(value)) {
            return 0.;
        }
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
    }
}
