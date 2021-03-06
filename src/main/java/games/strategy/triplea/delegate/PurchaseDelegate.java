package games.strategy.triplea.delegate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import games.strategy.engine.data.Change;
import games.strategy.engine.data.CompositeChange;
import games.strategy.engine.data.GameData;
import games.strategy.engine.data.NamedAttachable;
import games.strategy.engine.data.PlayerID;
import games.strategy.engine.data.ProductionRule;
import games.strategy.engine.data.RepairRule;
import games.strategy.engine.data.Resource;
import games.strategy.engine.data.Territory;
import games.strategy.engine.data.Unit;
import games.strategy.engine.data.UnitType;
import games.strategy.engine.data.changefactory.ChangeFactory;
import games.strategy.engine.message.IRemote;
import games.strategy.triplea.MapSupport;
import games.strategy.triplea.Properties;
import games.strategy.triplea.TripleAUnit;
import games.strategy.triplea.attachments.AbstractTriggerAttachment;
import games.strategy.triplea.attachments.ICondition;
import games.strategy.triplea.attachments.TechAbilityAttachment;
import games.strategy.triplea.attachments.TerritoryAttachment;
import games.strategy.triplea.attachments.TriggerAttachment;
import games.strategy.triplea.attachments.UnitAttachment;
import games.strategy.triplea.attachments.UnitTypeComparator;
import games.strategy.triplea.delegate.remote.IPurchaseDelegate;
import games.strategy.triplea.formatter.MyFormatter;
import games.strategy.util.CollectionUtils;
import games.strategy.util.IntegerMap;

/**
 * Logic for purchasing units.
 * Subclasses can override canAfford(...) to test if a purchase can be made
 * Subclasses can over ride addToPlayer(...) and removeFromPlayer(...) to change how
 * the adding or removing of resources is done.
 */
@MapSupport
public class PurchaseDelegate extends BaseTripleADelegate implements IPurchaseDelegate {
  private boolean m_needToInitialize = true;
  public static final String NOT_ENOUGH_RESOURCES = "Not enough resources";

  @Override
  public void start() {
    super.start();
    final GameData data = getData();
    if (m_needToInitialize) {
      if (Properties.getTriggers(data)) {
        // First set up a match for what we want to have fire as a default in this delegate. List out as a composite
        // match OR.
        // use 'null, null' because this is the Default firing location for any trigger that does NOT have 'when' set.
        final Predicate<TriggerAttachment> purchaseDelegateTriggerMatch = AbstractTriggerAttachment.availableUses
            .and(AbstractTriggerAttachment.whenOrDefaultMatch(null, null))
            .and(TriggerAttachment.prodMatch()
                .or(TriggerAttachment.prodFrontierEditMatch())
                .or(TriggerAttachment.purchaseMatch()));
        // get all possible triggers based on this match.
        final HashSet<TriggerAttachment> toFirePossible = TriggerAttachment.collectForAllTriggersMatching(
            new HashSet<>(Collections.singleton(m_player)), purchaseDelegateTriggerMatch);
        if (!toFirePossible.isEmpty()) {
          // get all conditions possibly needed by these triggers, and then test them.
          final HashMap<ICondition, Boolean> testedConditions =
              TriggerAttachment.collectTestsForAllTriggers(toFirePossible, m_bridge);
          // get all triggers that are satisfied based on the tested conditions.
          final Set<TriggerAttachment> toFireTestedAndSatisfied = new HashSet<>(
              CollectionUtils.getMatches(toFirePossible, AbstractTriggerAttachment.isSatisfiedMatch(testedConditions)));
          // now list out individual types to fire, once for each of the matches above.
          TriggerAttachment.triggerProductionChange(toFireTestedAndSatisfied, m_bridge, null, null, true, true, true,
              true);
          TriggerAttachment.triggerProductionFrontierEditChange(toFireTestedAndSatisfied, m_bridge, null, null, true,
              true, true, true);
          TriggerAttachment.triggerPurchase(toFireTestedAndSatisfied, m_bridge, null, null, true, true, true, true);
        }
      }
      m_needToInitialize = false;
    }
  }

  @Override
  public void end() {
    super.end();
    m_needToInitialize = true;
  }

  @Override
  public Serializable saveState() {
    final PurchaseExtendedDelegateState state = new PurchaseExtendedDelegateState();
    state.superState = super.saveState();
    state.m_needToInitialize = m_needToInitialize;
    return state;
  }

  @Override
  public void loadState(final Serializable state) {
    final PurchaseExtendedDelegateState s = (PurchaseExtendedDelegateState) state;
    super.loadState(s.superState);
    m_needToInitialize = s.m_needToInitialize;
  }

  @Override
  public boolean delegateCurrentlyRequiresUserInput() {
    if ((m_player.getProductionFrontier() == null || m_player.getProductionFrontier().getRules().isEmpty())
        && (m_player.getRepairFrontier() == null || m_player.getRepairFrontier().getRules().isEmpty())) {
      return false;
    }
    if (!canWePurchaseOrRepair()) {
      return false;
    }
    // if my capital is captured, I can't produce, but I may have PUs if I captured someone else's capital
    return TerritoryAttachment.doWeHaveEnoughCapitalsToProduce(m_player, getData());
  }

  protected boolean canWePurchaseOrRepair() {
    if (m_player.getProductionFrontier() != null && m_player.getProductionFrontier().getRules() != null) {
      for (final ProductionRule rule : m_player.getProductionFrontier().getRules()) {
        if (m_player.getResources().has(rule.getCosts())) {
          return true;
        }
      }
    }
    if (m_player.getRepairFrontier() != null && m_player.getRepairFrontier().getRules() != null) {
      for (final RepairRule rule : m_player.getRepairFrontier().getRules()) {
        if (m_player.getResources().has(rule.getCosts())) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * subclasses can over ride this method to use different restrictions as to what a player can buy.
   */
  protected boolean canAfford(final IntegerMap<Resource> costs, final PlayerID player) {
    return player.getResources().has(costs);
  }

  @Override
  public String purchase(final IntegerMap<ProductionRule> productionRules) {
    final IntegerMap<Resource> costs = getCosts(productionRules);
    final IntegerMap<NamedAttachable> results = getResults(productionRules);
    if (!(canAfford(costs, m_player))) {
      return NOT_ENOUGH_RESOURCES;
    }
    // check to see if player has too many of any building with a building limit
    final Iterator<NamedAttachable> iter2 = results.keySet().iterator();
    while (iter2.hasNext()) {
      final Object next = iter2.next();
      if (!(next instanceof Resource)) {
        final UnitType type = (UnitType) next;
        final int quantity = results.getInt(type);
        final UnitAttachment ua = UnitAttachment.get(type);
        final int maxBuilt = ua.getMaxBuiltPerPlayer();
        if (maxBuilt == 0) {
          return "May not build any of this unit right now: " + type.getName();
        } else if (maxBuilt > 0) {
          // count how many units are yet to be placed or are in the field
          int currentlyBuilt = m_player.getUnits().countMatches(Matches.unitIsOfType(type));

          final Predicate<Unit> unitTypeOwnedBy = Matches.unitIsOfType(type).and(Matches.unitIsOwnedBy(m_player));
          final Collection<Territory> allTerrs = getData().getMap().getTerritories();
          for (final Territory t : allTerrs) {
            currentlyBuilt += t.getUnits().countMatches(unitTypeOwnedBy);
          }

          final int allowedBuild = maxBuilt - currentlyBuilt;
          if (allowedBuild - quantity < 0) {
            return "May only build " + allowedBuild + " of " + type.getName() + " this turn, may only build " + maxBuilt
                + " total";
          }
        }
      }
    }
    // remove first, since add logs PUs remaining
    final Iterator<NamedAttachable> iter = results.keySet().iterator();
    final Collection<Unit> totalUnits = new ArrayList<>();
    final Collection<UnitType> totalUnitTypes = new ArrayList<>();
    final Collection<Resource> totalResources = new ArrayList<>();
    final Collection<NamedAttachable> totalAll = new ArrayList<>();
    final CompositeChange changes = new CompositeChange();
    // add changes for added resources
    // and find all added units
    while (iter.hasNext()) {
      final Object next = iter.next();
      if (next instanceof Resource) {
        final Resource resource = (Resource) next;
        final int quantity = results.getInt(resource);
        final Change change = ChangeFactory.changeResourcesChange(m_player, resource, quantity);
        changes.add(change);
        for (int i = 0; i < quantity; i++) {
          totalResources.add(resource);
        }
      } else {
        final UnitType type = (UnitType) next;
        final int quantity = results.getInt(type);
        final Collection<Unit> units = type.create(quantity, m_player);
        totalUnits.addAll(units);
        for (int i = 0; i < quantity; i++) {
          totalUnitTypes.add(type);
        }
      }
    }
    totalAll.addAll(totalUnitTypes);
    totalAll.addAll(totalResources);
    // add changes for added units
    if (!totalUnits.isEmpty()) {
      final Change change = ChangeFactory.addUnits(m_player, totalUnits);
      changes.add(change);
    }
    // add changes for spent resources
    final String remaining = removeFromPlayer(costs, changes);
    // add history event
    final String transcriptText;
    if (!totalUnits.isEmpty()) {
      transcriptText =
          m_player.getName() + " buy " + MyFormatter.defaultNamedToTextList(totalAll, ", ", true) + "; " + remaining;
    } else {
      transcriptText = m_player.getName() + " buy nothing; " + remaining;
    }
    m_bridge.getHistoryWriter().startEvent(transcriptText, totalUnits);
    // commit changes
    m_bridge.addChange(changes);
    return null;
  }

  @Override
  public String purchaseRepair(final Map<Unit, IntegerMap<RepairRule>> repairRules) {
    final IntegerMap<Resource> costs = getRepairCosts(repairRules, m_player);
    if (!(canAfford(costs, m_player))) {
      return NOT_ENOUGH_RESOURCES;
    }
    if (!Properties.getDamageFromBombingDoneToUnitsInsteadOfTerritories(getData())) {
      return null;
    }
    // Get the map of the factories that were repaired and how much for each
    final IntegerMap<Unit> repairMap = getUnitRepairs(repairRules);
    if (repairMap.isEmpty()) {
      return null;
    }
    // remove first, since add logs PUs remaining
    final CompositeChange changes = new CompositeChange();
    final Set<Unit> repairUnits = new HashSet<>(repairMap.keySet());
    final IntegerMap<Unit> damageMap = new IntegerMap<>();
    for (final Unit u : repairUnits) {
      final int repairCount = repairMap.getInt(u);
      // Display appropriate damaged/repaired factory and factory damage totals
      if (repairCount > 0) {
        final TripleAUnit taUnit = (TripleAUnit) u;
        final int newDamageTotal = Math.max(0, taUnit.getUnitDamage() - repairCount);
        if (newDamageTotal != taUnit.getUnitDamage()) {
          damageMap.put(u, newDamageTotal);
        }
      }
    }
    if (!damageMap.isEmpty()) {
      changes.add(ChangeFactory.bombingUnitDamage(damageMap));
    }
    // add changes for spent resources
    final String remaining = removeFromPlayer(costs, changes);
    // add history event
    final String transcriptText;
    if (!damageMap.isEmpty()) {
      transcriptText = m_player.getName() + " repair damage of "
          + MyFormatter.integerUnitMapToString(repairMap, ", ", "x ", true) + "; " + remaining;
    } else {
      transcriptText = m_player.getName() + " repair nothing; " + remaining;
    }
    m_bridge.getHistoryWriter().startEvent(transcriptText, new HashSet<>(damageMap.keySet()));
    // commit changes
    if (!changes.isEmpty()) {
      m_bridge.addChange(changes);
    }
    return null;
  }

  private IntegerMap<Unit> getUnitRepairs(final Map<Unit, IntegerMap<RepairRule>> repairRules) {
    final IntegerMap<Unit> repairMap = new IntegerMap<>();
    for (final Unit u : repairRules.keySet()) {
      final IntegerMap<RepairRule> rules = repairRules.get(u);
      final TreeSet<RepairRule> repRules = new TreeSet<>(repairRuleComparator);
      repRules.addAll(rules.keySet());
      for (final RepairRule repairRule : repRules) {
        final int quantity = rules.getInt(repairRule) * repairRule.getResults().getInt(u.getType());
        repairMap.add(u, quantity);
      }
    }
    return repairMap;
  }

  Comparator<RepairRule> repairRuleComparator = new Comparator<RepairRule>() {
    UnitTypeComparator utc = new UnitTypeComparator();

    @Override
    public int compare(final RepairRule o1, final RepairRule o2) {
      final UnitType u1 = (UnitType) o1.getResults().keySet().iterator().next();
      final UnitType u2 = (UnitType) o2.getResults().keySet().iterator().next();
      return utc.compare(u1, u2);
    }
  };

  private static IntegerMap<Resource> getCosts(final IntegerMap<ProductionRule> productionRules) {
    final IntegerMap<Resource> costs = new IntegerMap<>();
    final Iterator<ProductionRule> rules = productionRules.keySet().iterator();
    while (rules.hasNext()) {
      final ProductionRule rule = rules.next();
      costs.addMultiple(rule.getCosts(), productionRules.getInt(rule));
    }
    return costs;
  }

  private IntegerMap<Resource> getRepairCosts(final Map<Unit, IntegerMap<RepairRule>> repairRules,
      final PlayerID player) {
    final Collection<Unit> units = repairRules.keySet();
    final Iterator<Unit> iter = units.iterator();
    final IntegerMap<Resource> costs = new IntegerMap<>();
    while (iter.hasNext()) {
      final Unit u = iter.next();
      final Iterator<RepairRule> rules = repairRules.get(u).keySet().iterator();
      while (rules.hasNext()) {
        final RepairRule rule = rules.next();
        costs.addMultiple(rule.getCosts(), repairRules.get(u).getInt(rule));
      }
    }
    final double discount = TechAbilityAttachment.getRepairDiscount(player, getData());
    if (discount != 1.0D) {
      costs.multiplyAllValuesBy(discount, 3);
    }
    return costs;
  }

  private static IntegerMap<NamedAttachable> getResults(final IntegerMap<ProductionRule> productionRules) {
    final IntegerMap<NamedAttachable> costs = new IntegerMap<>();
    final Iterator<ProductionRule> rules = productionRules.keySet().iterator();
    while (rules.hasNext()) {
      final ProductionRule rule = rules.next();
      costs.addMultiple(rule.getResults(), productionRules.getInt(rule));
    }
    return costs;
  }

  @Override
  public Class<? extends IRemote> getRemoteType() {
    return IPurchaseDelegate.class;
  }

  protected String removeFromPlayer(final IntegerMap<Resource> costs, final CompositeChange changes) {
    final StringBuffer returnString = new StringBuffer("Remaining resources: ");
    final IntegerMap<Resource> left = m_player.getResources().getResourcesCopy();
    left.subtract(costs);
    for (final Entry<Resource, Integer> entry : left.entrySet()) {
      returnString.append(entry.getValue()).append(" ").append(entry.getKey().getName()).append("; ");
    }
    for (final Resource resource : costs.keySet()) {
      final float quantity = costs.getInt(resource);
      final int cost = (int) quantity;
      final Change change = ChangeFactory.changeResourcesChange(m_player, resource, -cost);
      changes.add(change);
    }
    return returnString.toString();
  }

}
