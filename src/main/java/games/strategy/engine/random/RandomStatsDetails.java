package games.strategy.engine.random;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import games.strategy.engine.data.PlayerID;
import games.strategy.util.IntegerMap;

public class RandomStatsDetails implements Serializable {
  private static final long serialVersionUID = 69602197220912520L;
  private final Map<PlayerID, IntegerMap<Integer>> m_data;
  private final IntegerMap<Integer> m_totalMap;
  private final DiceStatistic m_totalStats;
  private final Map<PlayerID, DiceStatistic> m_playerStats = new HashMap<>();

  RandomStatsDetails(final Map<PlayerID, IntegerMap<Integer>> randomStats, final int diceSides) {
    m_data = randomStats;
    m_totalMap = new IntegerMap<>();
    for (final Entry<PlayerID, IntegerMap<Integer>> entry : m_data.entrySet()) {
      m_totalMap.add(entry.getValue());
    }
    m_totalStats = getDiceStatistic(m_totalMap, diceSides);
    for (final Entry<PlayerID, IntegerMap<Integer>> entry : m_data.entrySet()) {
      m_playerStats.put(entry.getKey(), getDiceStatistic(entry.getValue(), diceSides));
    }
  }

  private static DiceStatistic getDiceStatistic(final IntegerMap<Integer> stats, final int diceSides) {
    final double average;
    final int total;
    final double median;
    final double stdDeviation;
    final double variance;
    if (stats.totalValues() != 0) {
      int sumTotal = 0;
      int localTotal = 0;
      // TODO: does this need to be updated to take data.getDiceSides() ?
      for (int i = 1; i <= diceSides; i++) {
        sumTotal += i * stats.getInt(i);
        localTotal += stats.getInt(i);
      }
      total = localTotal;
      average = (sumTotal) / ((double) stats.totalValues());
      // calculate median
      if (localTotal % 2 != 0) {
        median = calcMedian((localTotal / 2) + 1, diceSides, stats);
      } else {
        final double tmp1 = calcMedian((localTotal / 2), diceSides, stats);
        final double tmp2 = calcMedian((localTotal / 2) + 1, diceSides, stats);
        median = (tmp1 + tmp2) / 2;
      }
      // calculate variance
      double sumOfSquaredMeanDeviations = 0;
      // TODO: does this need to be updated to take data.getDiceSides() ?
      for (int i = 1; i <= diceSides; i++) {
        sumOfSquaredMeanDeviations += (stats.getInt(i) - (localTotal / diceSides))
            * (stats.getInt(i) - (localTotal / diceSides));
      }
      variance = sumOfSquaredMeanDeviations / (localTotal - 1);
      // calculate standard deviation
      stdDeviation = Math.sqrt(variance);
    } else {
      average = 0;
      total = 0;
      median = 0;
      stdDeviation = 0;
      variance = 0;
    }
    return new DiceStatistic(average, total, median, stdDeviation, variance);
  }

  public Map<PlayerID, IntegerMap<Integer>> getData() {
    return m_data;
  }

  private static int calcMedian(final int centerPoint, final int diceSides, final IntegerMap<Integer> stats) {
    int sum = 0;
    for (int i = 1; i <= diceSides; i++) {
      sum += stats.getInt(i);
      if (sum >= centerPoint) {
        return i;
      }
    }
    throw new AssertionError(String.format("Unexpected sum (%s) was never greater than center point (%s)",
        sum, centerPoint));
  }

  private static String getStatsString(final IntegerMap<Integer> diceRolls, final DiceStatistic diceStats,
      final String title, final String indentation) {
    final StringBuilder sb = new StringBuilder();
    sb.append(indentation).append(title).append("\n");
    for (final int key : new TreeSet<>(diceRolls.keySet())) {
      final int value = diceRolls.getInt(key);
      sb.append(indentation).append(indentation).append(indentation).append(key).append(" was rolled ").append(value)
          .append(" times").append("\n");
    }
    final DecimalFormat format = new DecimalFormat("#0.000");
    sb.append(indentation).append(indentation).append("Average roll : ").append(format.format(diceStats.getAverage()))
        .append("\n");
    sb.append(indentation).append(indentation).append("Median : ").append(format.format(diceStats.getMedian()))
        .append("\n");
    sb.append(indentation).append(indentation).append("Variance : ").append(format.format(diceStats.getVariance()))
        .append("\n");
    sb.append(indentation).append(indentation).append("Standard Deviation : ")
        .append(format.format(diceStats.getStdDeviation())).append("\n");
    sb.append(indentation).append(indentation).append("Total rolls : ").append(diceStats.getTotal()).append("\n");
    return sb.toString();
  }

  private static String getAllStatsString(final RandomStatsDetails details, final String indentation) {
    if (details.m_totalStats.getTotal() <= 0) {
      return "";
    }
    final StringBuilder sb = new StringBuilder();
    sb.append("Dice Statistics:\n\n");
    sb.append(getStatsString(details.m_totalMap, details.m_totalStats, "Total", indentation));
    if (details.getData().containsKey(null)) {
      sb.append("\n");
      sb.append(
          getStatsString(details.getData().get(null), details.m_playerStats.get(null), "Null / Other", indentation));
    }
    for (final Entry<PlayerID, IntegerMap<Integer>> entry : details.getData().entrySet()) {
      if (entry.getKey() == null) {
        continue;
      }
      sb.append("\n");
      sb.append(getStatsString(entry.getValue(), details.m_playerStats.get(entry.getKey()),
          (entry.getKey() == null ? "Null / Other" : entry.getKey().getName() + " Combat"), indentation));
    }
    return sb.toString();
  }

  public String getAllStatsString(final String indentation) {
    return getAllStatsString(this, indentation);
  }

  private static JPanel getStatsDisplay(final IntegerMap<Integer> diceRolls, final DiceStatistic diceStats,
      final String title) {
    final JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEtchedBorder());
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("<html><b>" + title + "</b></html>"));
    for (final int key : new TreeSet<>(diceRolls.keySet())) {
      final int value = diceRolls.getInt(key);
      final JLabel label = new JLabel(key + " was rolled " + value + " times");
      panel.add(label);
    }
    panel.add(new JLabel("  "));
    final DecimalFormat format = new DecimalFormat("#0.000");
    panel.add(new JLabel("Average roll : " + format.format(diceStats.getAverage())));
    panel.add(new JLabel("Median : " + format.format(diceStats.getMedian())));
    panel.add(new JLabel("Variance : " + format.format(diceStats.getVariance())));
    panel.add(new JLabel("Standard Deviation : " + format.format(diceStats.getStdDeviation())));
    panel.add(new JLabel("Total rolls : " + diceStats.getTotal()));
    return panel;
  }

  private static JPanel getAllStats(final RandomStatsDetails details) {
    final Insets insets = new Insets(2, 2, 2, 2);
    final JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder());
    panel.add(getStatsDisplay(details.m_totalMap, details.m_totalStats, "Total"), new GridBagConstraints(0, 0, 1,
        1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, insets, 0, 0));
    if (details.getData().containsKey(null)) {
      panel.add(getStatsDisplay(details.getData().get(null), details.m_playerStats.get(null), "Null / Other"),
          new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, insets,
              0, 0));
    }
    final int rows = Math.max(2, details.getData().size() / 6);
    int x = 0;
    for (final Entry<PlayerID, IntegerMap<Integer>> entry : details.getData().entrySet()) {
      if (entry.getKey() == null) {
        continue;
      }
      panel.add(
          getStatsDisplay(entry.getValue(), details.m_playerStats.get(entry.getKey()),
              (entry.getKey() == null ? "Null / Other" : entry.getKey().getName() + " Combat")),
          new GridBagConstraints((x / rows), 1 + (x % rows), 1, 1, 1, 1, GridBagConstraints.FIRST_LINE_START,
              GridBagConstraints.NONE, insets, 0, 0));
      x++;
    }
    return panel;
  }

  public JPanel getAllStats() {
    return getAllStats(this);
  }
}
