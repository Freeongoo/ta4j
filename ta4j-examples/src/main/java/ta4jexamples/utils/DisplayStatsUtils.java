package ta4jexamples.utils;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.*;
import org.ta4j.core.analysis.criteria.pnl.GrossReturnCriterion;
import org.ta4j.core.reports.TradingStatement;

import java.util.Collection;

/**
 * @author dorofeev
 */
public class DisplayStatsUtils {

    public static void printStats(Collection<TradingStatement> tradingStatements) {
        for (TradingStatement tradingStatement : tradingStatements) {
            printStat(tradingStatement);
        }
    }

    public static void printStat(TradingStatement tradingStatement) {
        Strategy strategy = tradingStatement.getStrategy();
        System.out.println("\nName of strategy: " +strategy.getName());

        System.out.println("totalProfitLoss: " + tradingStatement.getPerformanceReport().getTotalProfitLoss());
        System.out.println("totalProfitLossPercentage: " + tradingStatement.getPerformanceReport().getTotalProfitLossPercentage());
        System.out.println("totalLoss: " + tradingStatement.getPerformanceReport().getTotalLoss());
        System.out.println("totalProfit: " + tradingStatement.getPerformanceReport().getTotalProfit());
        System.out.println("lossCount: " + tradingStatement.getPositionStatsReport().getLossCount());
        System.out.println("profitCount: " + tradingStatement.getPositionStatsReport().getProfitCount());
    }

    public static void printStat(BarSeries series, TradingRecord tradingRecord) {
        System.out.println("Number of positions for the strategy: " + tradingRecord.getPositionCount());

        // Total profit
        GrossReturnCriterion totalReturn = new GrossReturnCriterion();
        System.out.println("Total return: " + totalReturn.calculate(series, tradingRecord));
        // Number of bars
        System.out.println("Number of bars: " + new NumberOfBarsCriterion().calculate(series, tradingRecord));
        // Average profit (per bar)
        System.out.println(
                "Average return (per bar): " + new AverageReturnPerBarCriterion().calculate(series, tradingRecord));
        // Number of positions
        System.out.println("Number of positions: " + new NumberOfPositionsCriterion().calculate(series, tradingRecord));
        // Profitable position ratio
        System.out.println(
                "Winning positions ratio: " + new WinningPositionsRatioCriterion().calculate(series, tradingRecord));
        // Maximum drawdown
        System.out.println("Maximum drawdown: " + new MaximumDrawdownCriterion().calculate(series, tradingRecord));
        // Reward-risk ratio
        System.out.println("Return over maximum drawdown: "
                + new ReturnOverMaxDrawdownCriterion().calculate(series, tradingRecord));
        // Total transaction cost
        System.out.println("Total transaction cost (from $1000): "
                + new LinearTransactionCostCriterion(1000, 0.005).calculate(series, tradingRecord));
        // Buy-and-hold
        System.out.println("Buy-and-hold return: " + new BuyAndHoldReturnCriterion().calculate(series, tradingRecord));
        // Total profit vs buy-and-hold
        System.out.println("Custom strategy return vs buy-and-hold strategy return: "
                + new VersusBuyAndHoldCriterion(totalReturn).calculate(series, tradingRecord));
    }
}
