package ta4jexamples.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 *
 */
public class PriceUtils {

    private static final int SCALE = 2;         // when min increase for stock is 0.01

    public static BigDecimal getSumTaxes(List<BigDecimal> commissions) {
        if (isEmpty(commissions)) {
            return new BigDecimal("0");
        }
        return PriceUtils.getCorrectRoundForPriceNullable(commissions.stream()
                .mapToDouble(t -> Math.abs(t.doubleValue()))
                .sum());
    }

    public static boolean isEqualPriceWithScaleInCent(BigDecimal price1, BigDecimal price2, BigDecimal centScale) {
        if (price1.equals(price2)) {
            return true;
        }

        return price1.subtract(price2).abs().compareTo(centScale) <= 0;
    }

    public static BigDecimal getCorrectRoundForPriceNullable(BigDecimal bigDecimal) {
        if (isNull(bigDecimal)) {
            return null;
        }
        return bigDecimal.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal getCorrectRoundForPriceNullable(Double number) {
        if (isNull(number)) {
            return null;
        }
        return new BigDecimal(number).setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static Double getCorrectRoundToDouble(BigDecimal bigDecimal) {
        if (isNull(bigDecimal)) {
            return null;
        }
        return bigDecimal.setScale(SCALE, RoundingMode.HALF_UP).doubleValue();
    }

    public static Double getCorrectRoundToDouble(Double value) {
        if (isNull(value)) {
            return null;
        }
        return new BigDecimal(value).setScale(SCALE, RoundingMode.HALF_UP).doubleValue();
    }
}
