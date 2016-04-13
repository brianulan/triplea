package util.triplea.MapXMLCreator;

import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import games.strategy.engine.data.GameParser;
import games.strategy.util.Triple;
import games.strategy.util.Tuple;
import util.image.FileOpen;
import util.image.FileSave;
import util.triplea.MapXMLCreator.TerritoryDefinitionDialog.DEFINITION;

/**
 * This class reads, writes and keeps the Map XML properties.
 */
public class MapXMLHelper {

  // TODO: explore builder pattern

  private static final String TRIPLEA_JAVA_CLASS_DELEGATE_PATH = "games.strategy.triplea.delegate.";
  static Map<String, String> xmlStrings = Maps.newLinkedHashMap();
  static List<String> resourceList = new ArrayList<String>();
  static Map<String, HashMap<DEFINITION, Boolean>> territoryDefintions =
      new HashMap<String, HashMap<DEFINITION, Boolean>>();
  static Map<String, LinkedHashSet<String>> territoryConnections = new HashMap<String, LinkedHashSet<String>>();
  static List<String> playerName = new ArrayList<String>();
  static Map<String, String> playerAlliance = new HashMap<String, String>();
  static Map<String, Integer> playerInitResources = new HashMap<String, Integer>();
  static Map<String, List<Integer>> unitDefinitions = Maps.newLinkedHashMap();
  static Map<String, List<String>> gamePlaySequence = Maps.newLinkedHashMap();
  static Map<String, Triple<String, String, Integer>> playerSequence =
      Maps.newLinkedHashMap();
  static Map<String, List<String>> technologyDefinitions =
      Maps.newLinkedHashMap();
  static Map<String, List<String>> productionFrontiers = Maps.newLinkedHashMap();
  static Map<String, List<String>> unitAttatchments = Maps.newLinkedHashMap();
  static Map<String, Integer> territoyProductions = new HashMap<String, Integer>();
  static Map<String, Tuple<SortedSet<String>, SortedSet<String>>> canalDefinitions =
      new HashMap<String, Tuple<SortedSet<String>, SortedSet<String>>>();
  static Map<String, String> territoryOwnerships = new HashMap<String, String>();
  static Map<String, Map<String, Map<String, Integer>>> unitPlacements =
      new HashMap<String, Map<String, Map<String, Integer>>>();
  static Map<String, List<String>> gameSettings = new HashMap<String, List<String>>();
  static String notes = "";

  static File mapXMLFile;

  static void putXmlStrings(final String key, final String value) {
    xmlStrings.put(key, value);
  }

  static void addResourceList(final String value) {
    resourceList.add(value);
  }

  static void addResourceList(final int index, final String value) {
    resourceList.add(index, value);
  }

  static void putTerritoryDefintions(final String key, final HashMap<DEFINITION, Boolean> value) {
    territoryDefintions.put(key, value);
  }

  static void putTerritoryConnections(final String key, final LinkedHashSet<String> value) {
    territoryConnections.put(key, value);
  }

  static void addPlayerName(final String value) {
    playerName.add(value);
  }

  static void putPlayerAlliance(final String key, final String value) {
    playerAlliance.put(key, value);
  }

  static void putPlayerInitResources(final String key, final Integer value) {
    playerInitResources.put(key, value);
  }

  static void putUnitDefinitions(final String key, final ArrayList<Integer> value) {
    unitDefinitions.put(key, value);
  }

  static void putGamePlaySequence(final String key, final ArrayList<String> value) {
    gamePlaySequence.put(key, value);
  }

  static void putPlayerSequence(final String key, final Triple<String, String, Integer> value) {
    playerSequence.put(key, value);
  }

  static void putTechnologyDefinitions(final String key, final ArrayList<String> value) {
    technologyDefinitions.put(key, value);
  }

  static void putProductionFrontiers(final String key, final ArrayList<String> value) {
    productionFrontiers.put(key, value);
  }

  static void putUnitAttatchments(final String key, final ArrayList<String> value) {
    unitAttatchments.put(key, value);
  }

  static void putTerritoyProductions(final String key, final Integer value) {
    territoyProductions.put(key, value);
  }

  static void putCanalDefinitions(final String key, final Tuple<SortedSet<String>, SortedSet<String>> value) {
    canalDefinitions.put(key, value);
  }

  static void putTerritoyOwnerships(final String key, final String value) {
    territoryOwnerships.put(key, value);
  }

  static void putUnitPlacements(final String key, final Map<String, Map<String, Integer>> value) {
    unitPlacements.put(key, value);
  }

  static void putGameSettings(final String key, final ArrayList<String> value) {
    gameSettings.put(key, value);
  }

  static void clearXmlStrings() {
    xmlStrings.clear();
  }

  static void clearResourceList() {
    resourceList.clear();
  }

  static void clearTerritoyDefintions() {
    territoryDefintions.clear();
  }

  static void clearTerritoryConnections() {
    territoryConnections.clear();
  }

  static void clearPlayerName() {
    playerName.clear();
  }

  static void clearPlayerAlliance() {
    playerAlliance.clear();
  }

  static void clearPlayerInitResources() {
    playerInitResources.clear();
  }

  static void clearUnitDefinitions() {
    unitDefinitions.clear();
  }

  static void clearGamePlaySequence() {
    gamePlaySequence.clear();
  }

  static void clearPlayerSequence() {
    playerSequence.clear();
  }

  static void clearTechnologyDefinitions() {
    technologyDefinitions.clear();
  }

  static void clearProductionFrontiers() {
    productionFrontiers.clear();
  }

  static void clearUnitAttatchments() {
    unitAttatchments.clear();
  }

  static void clearTerritoyProductions() {
    territoyProductions.clear();
  }

  static void clearCanalDefinitions() {
    canalDefinitions.clear();
  }

  static void clearTerritoyOwnerships() {
    territoryOwnerships.clear();
  }

  static void clearUnitPlacements() {
    unitPlacements.clear();
  }

  static void clearGameSettings() {
    gameSettings.clear();
  }

  static int loadXML() {
    try {
      Optional<String> gameXMLPath = Optional.empty();
      gameXMLPath = Optional.of(new FileOpen("Load a Game XML File", mapXMLFile, ".xml").getPathString());
      if (!gameXMLPath.isPresent()) {
        throw new FileNotFoundException("Could not load game XML file '" + mapXMLFile.getAbsolutePath() + "'");
      }
      System.out.println("Load Game XML from " + gameXMLPath);
      final FileInputStream in = new FileInputStream(gameXMLPath.get());

      // parse using builder to get DOM representation of the XML file
      Document dom = new GameParser().getDocument(in);

      int goToStep = parseValuesFromXML(dom);

      // set map file, image file and map folder
      MapXMLHelper.mapXMLFile = new File(gameXMLPath.get());
      File mapFolder = MapXMLHelper.mapXMLFile.getParentFile();
      if (mapFolder.getName().equals("games"))
        mapFolder = mapFolder.getParentFile();
      MapXMLCreator.mapFolderLocation = mapFolder;
      final File[] imageFiles = MapXMLCreator.mapFolderLocation.listFiles(new FilenameFilter() {

        @Override
        public boolean accept(File arg0, String arg1) {
          return (arg1.endsWith(".gif") || arg1.endsWith(".png"));
        }
      });
      if (imageFiles.length == 1)
        MapXMLCreator.mapImageFile = imageFiles[0];
      else {
        MapPropertiesPanel.selectMapImageFile();
      }

      final File fileGuess = new File(MapXMLCreator.mapFolderLocation, "centers.txt");
      if (fileGuess.exists()) {
        MapXMLCreator.mapCentersFile = fileGuess;
      } else
        MapPropertiesPanel.selectCentersFile();

      if (MapXMLCreator.mapImageFile == null || MapXMLCreator.mapCentersFile == null)
        goToStep = 1;

      ImageScrollPanePanel.polygonsInvalid = true;

      return goToStep;
    } catch (final IOException | HeadlessException | ParserConfigurationException | ParseException | SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return -1;
  }

  private static int parseValuesFromXML(Document dom) {
    initializeAll();

    final Node mainlastChild = dom.getLastChild();
    if (!mainlastChild.getNodeName().equals("game")) {
      throw new IllegalArgumentException(
          "Last node of XML document is not the expeced 'game' node, but '" + mainlastChild.getNodeName() + "'");
    }
    return parseGameNode(mainlastChild);
  }

  /**
   * @param gameNode
   * @return step to go to
   */
  public static int parseGameNode(final Node gameNode) {
    // TODO: stepToGo should become enum
    int stepToGo = -1;
    final NodeList children = gameNode.getChildNodes();
    for (int i = 0; i < children.getLength(); ++i) {
      final Node childNode = children.item(i);
      final String childNodeName = childNode.getNodeName();
      if (childNodeName.equals("info")) {
        final HashMap<String, String> infoAttr = getAttributesMap(childNode.getAttributes());
        for (final Entry<String, String> infoAttrEntry : infoAttr.entrySet())
          xmlStrings.put("info_@" + infoAttrEntry.getKey(), infoAttrEntry.getValue());
      } else if (childNodeName.equals("resourceList")) {
        NodeList resourceNodes = childNode.getChildNodes();

        for (int resource_i = 0; resource_i < resourceNodes.getLength(); ++resource_i) {
          final Node resourceNode = resourceNodes.item(resource_i);
          if (resourceNode.getNodeName().equals("resource"))
            resourceList.add(resourceNode.getAttributes().item(0).getNodeValue());
        }
      } else if (childNodeName.equals("map")) {
        parseMapNode(childNode.getChildNodes());
        stepToGo = Math.max(stepToGo, (territoryConnections.isEmpty() ? 2 : 3));
      } else if (childNodeName.equals("playerList")) {
        parsePlayerListNode(childNode.getChildNodes());
        stepToGo = Math.max(stepToGo, 4);
      } else if (childNodeName.equals("production")) {
        putNodesToProductionFrontiers(childNode.getChildNodes());
        stepToGo = Math.max(stepToGo, (productionFrontiers.isEmpty() ? 5 : 9));
      } else if (childNodeName.equals("gamePlay")) {
        putNodesToPlayerSequence(childNode.getChildNodes());
        stepToGo = Math.max(stepToGo, (playerSequence.isEmpty() ? 6 : 7));
      } else if (childNodeName.equals("attatchmentList")) {
        final NodeList attatchmentListChildNodes = childNode.getChildNodes();
        for (int p_i = 0; p_i < attatchmentListChildNodes.getLength(); ++p_i) {
          final Node attatchment = attatchmentListChildNodes.item(p_i);
          if (attatchment.getNodeName().equals("attatchment"))
            parseAttatchmentNode(attatchment);
        }
        stepToGo = Math.max(Math.max(stepToGo, unitAttatchments.isEmpty() ? 8 : 10),
            territoyProductions.isEmpty() ? 0 : 11);
      } else if (childNodeName.equals("initialize")) {
        final NodeList initializeChildNodes = childNode.getChildNodes();
        for (int init_i = 0; init_i < initializeChildNodes.getLength(); ++init_i) {
          final Node ownerInitialize = initializeChildNodes.item(init_i);
          if (ownerInitialize.getNodeName().equals("ownerInitialize")) {
            putNodesToTerritoryOwnerships(ownerInitialize.getChildNodes());
          } else if (ownerInitialize.getNodeName().equals("unitInitialize")) {
            putNodesToUnitPlacements(ownerInitialize.getChildNodes());
          }
        }
        stepToGo = Math.max(stepToGo, (unitPlacements.isEmpty() ? 13 : 14));
      } else if (childNodeName.equals("propertyList")) {
        final NodeList propertyListChildNodes = childNode.getChildNodes();
        for (int prop_i = 0; prop_i < propertyListChildNodes.getLength(); ++prop_i) {
          final Node property = propertyListChildNodes.item(prop_i);
          if (property.getNodeName().equals("property"))
            parsePropertyNode(property);
        }
        if (!gameSettings.isEmpty())
          stepToGo = (notes.length() > 0 ? 16 : 15);
      }
    }
    return stepToGo;
  }

  /**
   * 
   */
  public static void initializeAll() {
    MapXMLCreator.mapFolderLocation = null;
    MapXMLCreator.mapImageFile = null;
    MapXMLCreator.mapCentersFile = null;
    xmlStrings.clear();
    resourceList.clear();
    playerName.clear();
    playerAlliance.clear();
    playerInitResources.clear();
    territoryDefintions.clear();
    territoryConnections.clear();
    gamePlaySequence.clear();
    playerSequence.clear();
    productionFrontiers.clear();
    technologyDefinitions.clear();
    unitAttatchments.clear();
    territoyProductions.clear();
    canalDefinitions.clear();
    territoryOwnerships.clear();
    unitPlacements.clear();
    gameSettings.clear();
    notes = "";
  }

  /**
   * @param productionChildNodes
   */
  public static void putNodesToProductionFrontiers(final NodeList productionChildNodes) {
    for (int p_i = 0; p_i < productionChildNodes.getLength(); ++p_i) {
      final Node productionRule = productionChildNodes.item(p_i);
      if (productionRule.getNodeName().equals("productionRule"))
        parseProductionRuleNode(productionRule.getChildNodes());
      else if (productionRule.getNodeName().equals("productionFrontier")) {
        final String playerName = productionRule.getAttributes().getNamedItem("name").getNodeValue().substring(10);
        final ArrayList<String> frontierRules = new ArrayList<String>();
        final NodeList productionFrontierChildNodes = productionRule.getChildNodes();
        for (int pr_i = 0; pr_i < productionFrontierChildNodes.getLength(); ++pr_i) {
          final Node productionFrontierChildNode = productionFrontierChildNodes.item(pr_i);
          if (productionFrontierChildNode.getNodeName().equals("frontierRules")) {
            frontierRules
                .add(productionFrontierChildNode.getAttributes().getNamedItem("name").getNodeValue().substring(3));
          }
        }
        productionFrontiers.put(playerName, frontierRules);
      }
    }
  }

  public static void putNodesToPlayerSequence(final NodeList gamePlayChildNodes) {
    for (int p_i = 0; p_i < gamePlayChildNodes.getLength(); ++p_i) {
      final Node gamePlayChildNode = gamePlayChildNodes.item(p_i);
      if (gamePlayChildNode.getNodeName().equals("delegate")) {
        final HashMap<String, String> attrDelegate = getAttributesMap(gamePlayChildNode.getAttributes());
        final ArrayList<String> newValues = new ArrayList<String>();
        newValues.add(attrDelegate.get("javaClass").replace(TRIPLEA_JAVA_CLASS_DELEGATE_PATH, ""));
        newValues.add(attrDelegate.get("display"));
        gamePlaySequence.put(attrDelegate.get("name"), newValues);
      } else if (gamePlayChildNode.getNodeName().equals("sequence")) {
        final NodeList sequenceChildNodes = gamePlayChildNode.getChildNodes();
        for (int seq_i = 0; seq_i < sequenceChildNodes.getLength(); ++seq_i) {
          final Node sequenceChildNode = sequenceChildNodes.item(seq_i);
          if (sequenceChildNode.getNodeName().equals("step")) {
            final HashMap<String, String> attrSequence = getAttributesMap(sequenceChildNode.getAttributes());
            final String maxRunCount = attrSequence.get("maxRunCount");
            final String player = attrSequence.get("player");
            final Triple<String, String, Integer> newValues = Triple.of(attrSequence.get("delegate"),
                (player == null ? "" : player), (maxRunCount == null ? 0 : Integer.parseInt(maxRunCount)));
            playerSequence.put(attrSequence.get("name"), newValues);
          }
        }
      }
    }
  }

  /**
   * @param ownerInitialize
   */
  public static void putNodesToUnitPlacements(final NodeList initializeUnitChildNodes) {
    for (int initOwner_i = 0; initOwner_i < initializeUnitChildNodes.getLength(); ++initOwner_i) {
      final Node unitPlacement = initializeUnitChildNodes.item(initOwner_i);
      if (unitPlacement.getNodeName().equals("unitPlacement")) {
        final HashMap<String, String> attrUnitPlacements = getAttributesMap(unitPlacement.getAttributes());
        final String territory = attrUnitPlacements.get("territory");
        final String owner = attrUnitPlacements.get("owner");
        Map<String, Map<String, Integer>> terrPlacements = unitPlacements.get(territory);
        if (terrPlacements == null) {
          terrPlacements = new HashMap<String, Map<String, Integer>>();
          unitPlacements.put(territory, terrPlacements);
        }
        Map<String, Integer> terrOwnerPlacements = terrPlacements.get(owner);
        if (terrOwnerPlacements == null) {
          terrOwnerPlacements = Maps.newLinkedHashMap();
          terrPlacements.put(owner, terrOwnerPlacements);
        }
        terrOwnerPlacements.put(attrUnitPlacements.get("unitType"),
            Integer.parseInt(attrUnitPlacements.get("quantity")));
      }
    }
  }

  public static void putNodesToTerritoryOwnerships(final NodeList initializeOwnerChildNodes) {
    for (int initOwner_i = 0; initOwner_i < initializeOwnerChildNodes.getLength(); ++initOwner_i) {
      final Node territoryOwner = initializeOwnerChildNodes.item(initOwner_i);
      if (territoryOwner.getNodeName().equals("territoryOwner")) {
        final HashMap<String, String> attrTerrOwner = getAttributesMap(territoryOwner.getAttributes());
        territoryOwnerships.put(attrTerrOwner.get("territory"), attrTerrOwner.get("owner"));
      }
    }
  }

  private static void parsePropertyNode(final Node property) {
    final HashMap<String, String> propertyAttr = getAttributesMap(property.getAttributes());
    final ArrayList<String> settingValues = new ArrayList<String>();
    final String propertyName = propertyAttr.get("name");
    if (propertyName.equals("notes") || propertyName.equals("mapName")) {
      final NodeList propertyListChildNodes = property.getChildNodes();
      for (int prop_i = 0; prop_i < propertyListChildNodes.getLength(); ++prop_i) {
        final Node subProperty = propertyListChildNodes.item(prop_i);
        if (subProperty.getNodeName().equals("value"))
          notes = subProperty.getTextContent();
      }
      return;
    }
    settingValues.add(propertyAttr.get("value"));
    settingValues.add(Boolean.toString(Boolean.parseBoolean(propertyAttr.get("editable"))));
    final NodeList propertyNodes = property.getChildNodes();
    for (int pr_i = 0; pr_i < propertyNodes.getLength(); ++pr_i) {
      final Node propertyRange = propertyNodes.item(pr_i);
      if (propertyRange.getNodeName().equals("number")) {
        final HashMap<String, String> propertyRangeAttr = getAttributesMap(propertyRange.getAttributes());
        settingValues.add(propertyRangeAttr.get("min"));
        settingValues.add(propertyRangeAttr.get("max"));
        gameSettings.put(propertyName, settingValues);
        break;
      } else if (propertyRange.getNodeName().equals("boolean")) {
        settingValues.add("0"); // min
        settingValues.add("0"); // max
        gameSettings.put(propertyName, settingValues);
        break;
      }
    }
  }

  private static void parseAttatchmentNode(final Node attatchment) {
    final HashMap<String, String> attatchmentAttr = getAttributesMap(attatchment.getAttributes());
    final String attatachmentName = attatchmentAttr.get("name");
    final String attatachmentType = attatchmentAttr.get("type");
    final String attatachmentAttatchTo = attatchmentAttr.get("attatchTo");
    if (attatachmentName.equals("techAttatchment") && attatachmentType.equals("player")) {
      final NodeList attatchmentOptionNodes = attatchment.getChildNodes();
      for (int pr_i = 0; pr_i < attatchmentOptionNodes.getLength(); ++pr_i) {
        final Node attatchmentOption = attatchmentOptionNodes.item(pr_i);
        if (attatchmentOption.getNodeName().equals("option")) {
          final HashMap<String, String> attatchmentOptionAttr = getAttributesMap(attatchmentOption.getAttributes());
          final ArrayList<String> values = new ArrayList<String>();
          values.add(attatachmentAttatchTo); // playerName
          values.add(attatchmentOptionAttr.get("value"));
          technologyDefinitions.put(attatchmentOptionAttr.get("name") + "_" + attatachmentAttatchTo, values);
        }
      }
    } else if (attatachmentName.equals("unitAttatchment") && attatachmentType.equals("unitType")) {
      final NodeList attatchmentOptionNodes = attatchment.getChildNodes();
      for (int pr_i = 0; pr_i < attatchmentOptionNodes.getLength(); ++pr_i) {
        final Node attatchmentOption = attatchmentOptionNodes.item(pr_i);
        if (attatchmentOption.getNodeName().equals("option")) {
          final HashMap<String, String> attatchmentOptionAttr = getAttributesMap(attatchmentOption.getAttributes());
          final ArrayList<String> values = new ArrayList<String>();
          values.add(attatachmentAttatchTo); // unitName
          values.add(attatchmentOptionAttr.get("value"));
          unitAttatchments.put(attatchmentOptionAttr.get("name") + "_" + attatachmentAttatchTo, values);
        }
      }
    } else if (attatachmentName.equals("canalAttatchment") && attatachmentType.equals("territory")) {
      final NodeList attatchmentOptionNodes = attatchment.getChildNodes();

      Tuple<SortedSet<String>, SortedSet<String>> canalDef = null;
      String newCanalName = null;
      SortedSet<String> newLandTerritories = new TreeSet<String>();
      for (int pr_i = 0; pr_i < attatchmentOptionNodes.getLength(); ++pr_i) {
        final Node attatchmentOption = attatchmentOptionNodes.item(pr_i);
        if (attatchmentOption.getNodeName().equals("option")) {
          final HashMap<String, String> attatchmentOptionAttr = getAttributesMap(attatchmentOption.getAttributes());
          final String attatOptAttrName = attatchmentOptionAttr.get("name");
          if (attatOptAttrName.equals("canalName")) {
            newCanalName = attatchmentOptionAttr.get("value");
            canalDef = canalDefinitions.get(newCanalName);
            if (canalDef != null)
              break;
          } else if (attatOptAttrName.equals("landTerritories")) {
            newLandTerritories.addAll(Arrays.asList(attatchmentOptionAttr.get("value").split(":")));
          }
        }
      }
      if (canalDef == null) {
        final SortedSet<String> newWaterTerritories = new TreeSet<String>();
        newWaterTerritories.add(attatachmentAttatchTo);
        canalDefinitions.put(newCanalName, Tuple.of(newWaterTerritories, newLandTerritories));
      } else
        canalDef.getFirst().add(attatachmentAttatchTo);
    } else if (attatachmentName.equals("territoryAttatchment") && attatachmentType.equals("territory")) {
      final NodeList attatchmentOptionNodes = attatchment.getChildNodes();
      for (int pr_i = 0; pr_i < attatchmentOptionNodes.getLength(); ++pr_i) {
        final Node attatchmentOption = attatchmentOptionNodes.item(pr_i);
        if (attatchmentOption.getNodeName().equals("option")) {
          final HashMap<String, String> attatchmentOptionAttr = getAttributesMap(attatchmentOption.getAttributes());
          final String optionNameAttr = attatchmentOptionAttr.get("name");
          if (optionNameAttr.equals("production"))
            territoyProductions.put(attatachmentAttatchTo, Integer.parseInt(attatchmentOptionAttr.get("value")));
          else {
            HashMap<DEFINITION, Boolean> terrDefinitions = territoryDefintions.get(attatachmentAttatchTo);
            if (terrDefinitions == null) {
              terrDefinitions = new HashMap<TerritoryDefinitionDialog.DEFINITION, Boolean>();
              territoryDefintions.put(attatachmentAttatchTo, terrDefinitions);
            }
            switch (TerritoryDefinitionDialog.valueOf(optionNameAttr)) {
              case IS_CAPITAL:
                terrDefinitions.put(DEFINITION.IS_CAPITAL, true);
                break;
              case IS_VICTORY_CITY:
                terrDefinitions.put(DEFINITION.IS_VICTORY_CITY, true);
                break;
              case IS_WATER:
                terrDefinitions.put(DEFINITION.IS_WATER, true);
                break;
              case IMPASSABLE:
                terrDefinitions.put(DEFINITION.IMPASSABLE, true);
                break;
            }
          }
        }
      }
    }
  }

  private static void parseProductionRuleNode(final NodeList productionRuleChildNodes) {
    HashMap<String, String> attrMapCost = null;
    HashMap<String, String> attrMapResult = null;
    for (int pr_i = 0; pr_i < productionRuleChildNodes.getLength(); ++pr_i) {
      final Node productionRuleChildNode = productionRuleChildNodes.item(pr_i);
      final String productionRuleChildNodeName = productionRuleChildNode.getNodeName();
      if (productionRuleChildNodeName.equals("cost")) {
        attrMapCost = getAttributesMap(productionRuleChildNode.getAttributes());
        if (attrMapResult != null)
          break;
      } else if (productionRuleChildNodeName.equals("result")) {
        attrMapResult = getAttributesMap(productionRuleChildNode.getAttributes());
        if (attrMapCost != null)
          break;
      }
    }
    final ArrayList<Integer> newValues = new ArrayList<Integer>();
    newValues.add(Integer.parseInt(attrMapCost.get("quantity")));
    newValues.add(Integer.parseInt(attrMapResult.get("quantity")));
    MapXMLHelper.unitDefinitions.put(attrMapResult.get("resourceOrUnit"), newValues);
  }

  private static HashMap<String, String> getAttributesMap(final NamedNodeMap attrNodeMap) {
    final HashMap<String, String> rVal = new HashMap<String, String>();
    for (int i = 0; i < attrNodeMap.getLength(); ++i) {
      final Node attrNodeItem = attrNodeMap.item(i);
      rVal.put(attrNodeItem.getNodeName(), attrNodeItem.getNodeValue());
    }
    return rVal;
  }

  private static void parsePlayerListNode(final NodeList playerListChildNodes) {
    for (int pl_i = 0; pl_i < playerListChildNodes.getLength(); ++pl_i) {
      final Node playerListChildNode = playerListChildNodes.item(pl_i);
      final String playerListChildNodeName = playerListChildNode.getNodeName();
      if (playerListChildNodeName.equals("player")) {
        final HashMap<String, String> attrMapPlayer = getAttributesMap(playerListChildNode.getAttributes());

        final String playerNameAttr = attrMapPlayer.get("name");
        playerName.add(playerNameAttr);
        playerInitResources.put(playerNameAttr, 0);
        // TODO: add logic for optional value
        // attrMapPlayer.get("optional")
      } else if (playerListChildNodeName.equals("alliance")) {
        final HashMap<String, String> attrMapPlayer = getAttributesMap(playerListChildNode.getAttributes());
        playerAlliance.put(attrMapPlayer.get("player"), attrMapPlayer.get("alliance"));
      }
    }
  }

  private static void parseMapNode(NodeList mapChildNodes) {
    for (int map_i = 0; map_i < mapChildNodes.getLength(); ++map_i) {
      final Node mapChildNode = mapChildNodes.item(map_i);
      final String mapChildNodeName = mapChildNode.getNodeName();
      if (mapChildNodeName.equals("territory")) {
        final NamedNodeMap terrAttrNodes = mapChildNode.getAttributes();
        String terrName = null;
        final HashMap<DEFINITION, Boolean> terrDef = new HashMap<TerritoryDefinitionDialog.DEFINITION, Boolean>();
        for (int terrAttr_i = 0; terrAttr_i < terrAttrNodes.getLength(); ++terrAttr_i) {
          final Node terrAttrNode = terrAttrNodes.item(terrAttr_i);
          if (terrAttrNode.getNodeName().equals("name")) {
            terrName = terrAttrNode.getNodeValue();
          } else {
            terrDef.put(TerritoryDefinitionDialog.valueOf(terrAttrNode.getNodeName()),
                Boolean.valueOf(terrAttrNode.getNodeValue()));
          }
        }
        territoryDefintions.put(terrName, terrDef);
      } else if (mapChildNodeName.equals("connection")) {
        final NamedNodeMap connectionAttrNodes = mapChildNode.getAttributes();
        String t1Name = connectionAttrNodes.item(0).getNodeValue();
        String t2Name = connectionAttrNodes.item(1).getNodeValue();
        if (t1Name.compareTo(t2Name) > 0) {
          final String swapHelper = t1Name;
          t1Name = t2Name;
          t2Name = swapHelper;
        }
        LinkedHashSet<String> t1Connections = territoryConnections.get(t1Name);
        if (t1Connections != null)
          t1Connections.add(t2Name);
        else {
          t1Connections = new LinkedHashSet<String>();
          t1Connections.add(t2Name);
          territoryConnections.put(t1Name, t1Connections);
        }
      }
    }
  }

  static void saveXML() {
    try {
      final String fileName = new FileSave("Where to Save the Game XML ?", xmlStrings.get("info_@name") + ".xml",
          MapXMLCreator.mapFolderLocation).getPathString();
      if (fileName == null) {
        return;
      }

      // write the content into xml file
      final TransformerFactory transformerFactory = TransformerFactory.newInstance();
      final Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, GameParser.dtdFileName);
      final DOMSource source = new DOMSource(getXMLDocument());
      final File newFile = new File(fileName);
      final StreamResult result = new StreamResult(newFile);

      // Output to console for testing
      // StreamResult result = new StreamResult(System.out);

      transformer.transform(source, result);

      System.out.println("");
      System.out.println("Game XML written to " + newFile.getCanonicalPath());
    } catch (final IOException | HeadlessException | TransformerException | ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static Document getXMLDocument() throws ParserConfigurationException {
    // TODO: break into smaller chunks, consider builder pattern
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();

    Document doc = db.newDocument();
    Element game = doc.createElement("game");
    doc.appendChild(game);

    appendFromXMLStrings(doc, game);

    Element map = doc.createElement("map");
    game.appendChild(map);


    map.appendChild(doc.createComment(" Territory Definitions "));

    boolean territoryAttatchmentNeeded = appendFromTerritoryDefinitions(doc, map);

    appendFromResourceList(doc, game);

    appendFromPlayerName(doc, game);

    appendFromUnitDefinitions(doc, game, map);

    appendFromGamePlaySequence(doc, game);

    if (!territoyProductions.isEmpty())
      territoryAttatchmentNeeded = true;

    if (!technologyDefinitions.isEmpty() || !unitAttatchments.isEmpty() || territoryAttatchmentNeeded
        || !canalDefinitions.isEmpty()) {
      final Element attatchmentList = doc.createElement("attatchmentList");
      game.appendChild(attatchmentList);

      appendToAttachmentList(doc, territoryAttatchmentNeeded, attatchmentList);
    }

    final Element initialize = doc.createElement("initialize");
    game.appendChild(initialize);

    appendFromTerritoryOwnerships(doc, initialize);

    appendFromUnitPlacements(doc, initialize);

    final Element propertyList = doc.createElement("propertyList");
    game.appendChild(propertyList);

    appendFromGameSettings(doc, propertyList);

    if (notes.length() > 0) {
      final Element property = doc.createElement("property");
      property.setAttribute("name", "notes");
      final Element propertyValue = doc.createElement("value");
      propertyValue.setTextContent(notes);
      property.appendChild(propertyValue);
      propertyList.appendChild(property);
    }

    if (mapXMLFile != null) {
      propertyList.appendChild(doc.createComment(" Map Name: also used for map utils when asked "));
      final Element property = doc.createElement("property");
      property.setAttribute("name", "mapName");
      final String fileName = mapXMLFile.getName();
      property.setAttribute("value", fileName.substring(0, fileName.lastIndexOf(".") - 1));
      property.setAttribute("editable", "false");
      propertyList.appendChild(property);
    }

    return doc;
  }

  public static void appendFromGameSettings(Document doc, final Element propertyList) {
    if (!gameSettings.isEmpty()) {
      for (final Entry<String, List<String>> gameSettingsEntry : gameSettings.entrySet()) {
        final Element property = doc.createElement("property");
        final List<String> settingsValue = gameSettingsEntry.getValue();
        property.setAttribute("name", gameSettingsEntry.getKey());
        final String valueString = settingsValue.get(0);
        property.setAttribute("value", valueString);
        property.setAttribute("editable", settingsValue.get(1));
        if (valueString.equals("true") || valueString.equals("false")) {
          property.appendChild(doc.createElement("boolean"));
        } else {
          final String minString = settingsValue.get(2);
          final String maxString = settingsValue.get(3);
          try {
            Integer.valueOf(minString);
            Integer.valueOf(maxString);
            final Element number = doc.createElement("number");
            number.setAttribute("min", minString);
            number.setAttribute("max", maxString);
            property.appendChild(number);
          } catch (NumberFormatException nfe) {
            // nothing to do
          }
        }
        propertyList.appendChild(property);
      }
    }
  }

  public static void appendFromUnitPlacements(Document doc, final Element initialize) {
    if (!unitPlacements.isEmpty()) {
      final Element unitInitialize = doc.createElement("unitInitialize");
      initialize.appendChild(unitInitialize);
      for (final Entry<String, Map<String, Map<String, Integer>>> placementEntry : unitPlacements
          .entrySet()) {
        final Element unitPlacementTemplate = doc.createElement("unitPlacement");
        unitPlacementTemplate.setAttribute("territory", placementEntry.getKey());
        for (final Entry<String, Map<String, Integer>> playerPlacements : placementEntry.getValue()
            .entrySet()) {
          final Element playerUnitPlacementTemplate = (Element) unitPlacementTemplate.cloneNode(false);
          if (playerPlacements.getKey() != null)
            playerUnitPlacementTemplate.setAttribute("owner", playerPlacements.getKey());
          for (final Entry<String, Integer> unitDetails : playerPlacements.getValue().entrySet()) {
            if (unitDetails.getValue() > 0) {
              final Element unitPlacement = (Element) playerUnitPlacementTemplate.cloneNode(false);
              unitPlacement.setAttribute("unitType", unitDetails.getKey());
              unitPlacement.setAttribute("quantity", unitDetails.getValue().toString());
              unitInitialize.appendChild(unitPlacement);
            }
          }
        }
      }
    }
  }

  public static void appendFromTerritoryOwnerships(Document doc, final Element initialize) {
    if (!territoryOwnerships.isEmpty()) {
      final Element ownerInitialize = doc.createElement("ownerInitialize");
      initialize.appendChild(ownerInitialize);
      final HashMap<String, ArrayList<String>> playerTerritories = new HashMap<String, ArrayList<String>>();
      for (final String player : playerName)
        playerTerritories.put(player, new ArrayList<String>());
      for (final Entry<String, String> ownershipEntry : territoryOwnerships.entrySet())
        playerTerritories.get(ownershipEntry.getValue()).add(ownershipEntry.getKey());
      for (final Entry<String, ArrayList<String>> playerTerritoriesEntry : playerTerritories.entrySet()) {
        doc.createComment(" " + playerTerritoriesEntry.getKey() + " Owned Territories ");
        final Element territoryOwnerTemplate = doc.createElement("territoryOwner");
        territoryOwnerTemplate.setAttribute("owner", playerTerritoriesEntry.getKey());
        for (final String territory : playerTerritoriesEntry.getValue()) {
          final Element territoryOwner = (Element) territoryOwnerTemplate.cloneNode(false);
          territoryOwner.setAttribute("territory", territory);
          ownerInitialize.appendChild(territoryOwner);
        }
      }
    }
  }

  public static void appendFromResourceList(Document doc, Element game) {
    if (!resourceList.isEmpty()) {
      Element resourceListElement = doc.createElement("resourceList");
      game.appendChild(resourceListElement);
      for (final String resourceName : resourceList) {
        final Element resourceElement = doc.createElement("resource");
        resourceElement.setAttribute("name", resourceName);
        resourceListElement.appendChild(resourceElement);
      }
    }
  }

  public static void appendToAttachmentList(Document doc, boolean territoryAttatchmentNeeded,
      final Element attatchmentList) {
    final Element attatchmentTemplate = doc.createElement("attatchment");
    if (!technologyDefinitions.isEmpty()) {
      attatchmentTemplate.setAttribute("name", "techAttatchment");
      attatchmentTemplate.setAttribute("javaClass", "games.strategy.triplea.attatchments.TechAttachment");
      attatchmentTemplate.setAttribute("type", "player");
      writeAttatchmentNodes(doc, attatchmentList, technologyDefinitions, attatchmentTemplate);
    }
    if (!unitAttatchments.isEmpty()) {
      attatchmentTemplate.setAttribute("name", "unitAttatchment");
      attatchmentTemplate.setAttribute("javaClass", "games.strategy.triplea.attatchments.UnitAttachment");
      attatchmentTemplate.setAttribute("type", "unitType");
      writeAttatchmentNodes(doc, attatchmentList, unitAttatchments, attatchmentTemplate);
    }
    if (territoryAttatchmentNeeded) {
      attatchmentTemplate.setAttribute("name", "territoryAttatchment");
      attatchmentTemplate.setAttribute("javaClass", "games.strategy.triplea.attatchments.TerritoryAttachment");
      attatchmentTemplate.setAttribute("type", "territory");
      Map<String, List<String>> territoryAttatchments = Maps.newLinkedHashMap();
      for (final Entry<String, HashMap<DEFINITION, Boolean>> territoryDefinition : territoryDefintions.entrySet()) {
        final String territoryName = territoryDefinition.getKey();
        for (final Entry<DEFINITION, Boolean> definition : territoryDefinition.getValue().entrySet()) {
          if (definition.getValue() == Boolean.TRUE) {
            final ArrayList<String> attatchmentOptions = new ArrayList<String>();
            attatchmentOptions.add(territoryName);
            attatchmentOptions.add("true");
            // TODO: handle capital different based on owner
            // owner defined (step 13) only after territory definitions (step 2)
            // <option name="capital" value="Italians"/>
            territoryAttatchments.put(
                TerritoryDefinitionDialog.getDefinitionString(definition.getKey()) + "_" + territoryName,
                attatchmentOptions);
          }
        }
      }
      for (final Entry<String, Integer> productionEntry : territoyProductions.entrySet()) {
        final Integer production = productionEntry.getValue();
        if (production > 0) {
          final String territoryName = productionEntry.getKey();
          final ArrayList<String> attatchmentOptions = new ArrayList<String>();
          attatchmentOptions.add(territoryName);
          attatchmentOptions.add(production.toString());
          territoryAttatchments.put("production_" + territoryName, attatchmentOptions);
        }
      }
      writeAttatchmentNodes(doc, attatchmentList, territoryAttatchments, attatchmentTemplate);
    }
    if (!canalDefinitions.isEmpty()) {
      attatchmentTemplate.setAttribute("name", "canalAttatchment");
      attatchmentTemplate.setAttribute("javaClass", "games.strategy.triplea.attatchments.CanalAttachment");
      attatchmentTemplate.setAttribute("type", "territory");
      for (final Entry<String, Tuple<SortedSet<String>, SortedSet<String>>> canalDefEntry : canalDefinitions
          .entrySet()) {
        final Tuple<SortedSet<String>, SortedSet<String>> canalDef = canalDefEntry.getValue();
        Iterator<String> iter_landTerrs = canalDef.getSecond().iterator();
        final StringBuilder sb = new StringBuilder(iter_landTerrs.next());
        while (iter_landTerrs.hasNext())
          sb.append(":").append(iter_landTerrs.next());
        final String landTerritories = sb.toString();

        final Element canalOptionName = doc.createElement("option");
        canalOptionName.setAttribute("name", "canalName");
        canalOptionName.setAttribute("value", canalDefEntry.getKey());
        attatchmentTemplate.appendChild(canalOptionName);
        final Element canalOptionLandTerrs = doc.createElement("option");
        canalOptionLandTerrs.setAttribute("name", "landTerritories");
        canalOptionLandTerrs.setAttribute("value", landTerritories);
        attatchmentTemplate.appendChild(canalOptionLandTerrs);
        for (final String waterTerr : canalDef.getFirst()) {
          final Element canalAttatchment = (Element) attatchmentTemplate.cloneNode(true);
          canalAttatchment.setAttribute("attatchTo", waterTerr);
          attatchmentList.appendChild(canalAttatchment);
        }
      }
      writeAttatchmentNodes(doc, attatchmentList, unitAttatchments, attatchmentTemplate);
    }
  }

  public static boolean appendFromTerritoryDefinitions(Document doc, Element map) {
    boolean territoryAttatchmentNeeded = false;
    for (final Entry<String, HashMap<DEFINITION, Boolean>> entryTerritoryDefinition : territoryDefintions.entrySet()) {
      Element territory = doc.createElement("territory");
      territory.setAttribute("name", entryTerritoryDefinition.getKey());
      final HashMap<DEFINITION, Boolean> territoryDefinition = entryTerritoryDefinition.getValue();
      final int territoryDefinitionSize = territoryDefinition.size();
      final Boolean isWater = territoryDefinition.get(DEFINITION.IS_WATER);
      if (isWater != null && isWater) {
        territory.setAttribute(TerritoryDefinitionDialog.getDefinitionString(DEFINITION.IS_WATER), "true");
        if (territoryDefinitionSize > 1)
          territoryAttatchmentNeeded = true;
      } else if (territoryDefinitionSize > 1 || isWater == null && territoryDefinitionSize > 0)
        territoryAttatchmentNeeded = true;
      map.appendChild(territory);
    }

    map.appendChild(doc.createComment(" Territory Connections "));
    for (final Entry<String, LinkedHashSet<String>> entryTerritoryConnection : territoryConnections.entrySet()) {
      final Element connectionTemp = doc.createElement("connection");
      connectionTemp.setAttribute("t1", entryTerritoryConnection.getKey());
      for (final String t2 : entryTerritoryConnection.getValue()) {
        connectionTemp.setAttribute("t2", t2);
        map.appendChild(connectionTemp.cloneNode(false));
      }
    }
    return territoryAttatchmentNeeded;
  }

  public static void appendFromXMLStrings(Document doc, Element game) {
    Element currentElem = null;
    String prevKeyNode = null;
    for (final Entry<String, String> entryXMLString : xmlStrings.entrySet()) {
      String key = entryXMLString.getKey();
      String[] split = key.split("_@");
      if (!split[0].equals(prevKeyNode)) {
        Element parentElem = game;
        for (String newNode : split[0].split("_")) {
          currentElem = doc.createElement(newNode);
          parentElem.appendChild(currentElem);
          parentElem = currentElem;
        }
      }
      currentElem.setAttribute(split[1], entryXMLString.getValue());
      prevKeyNode = split[0];
    }
  }

  public static void appendFromGamePlaySequence(Document doc, Element game) {
    if (!gamePlaySequence.isEmpty()) {
      final Element gamePlay = doc.createElement("gamePlay");
      game.appendChild(gamePlay);

      for (final Entry<String, List<String>> delegateStep : gamePlaySequence.entrySet()) {
        final List<String> delegateProperties = delegateStep.getValue();
        final Element delegate = doc.createElement("delegate");
        delegate.setAttribute("name", delegateStep.getKey());
        delegate.setAttribute("javaClass", TRIPLEA_JAVA_CLASS_DELEGATE_PATH + delegateProperties.get(0));
        delegate.setAttribute("display", delegateProperties.get(1));
        gamePlay.appendChild(delegate);
      }

      if (!playerSequence.isEmpty()) {
        final Element sequence = doc.createElement("sequence");
        gamePlay.appendChild(sequence);

        for (final Entry<String, Triple<String, String, Integer>> sequenceStep : playerSequence.entrySet()) {
          final Triple<String, String, Integer> sequenceProperties = sequenceStep.getValue();
          final Element step = doc.createElement("step");
          final String sequenceStepKey = sequenceStep.getKey();
          step.setAttribute("name", sequenceStepKey);
          step.setAttribute("delegate", sequenceProperties.getFirst());
          if (sequenceProperties.getSecond().length() > 0)
            step.setAttribute("player", sequenceProperties.getSecond());
          final Integer maxRunCount = sequenceProperties.getThird();
          if (maxRunCount > 0)
            step.setAttribute("maxRunCount", maxRunCount.toString());
          if (sequenceStepKey.endsWith("NonCombatMove"))
            step.setAttribute("display", "Non Combat Move");
          sequence.appendChild(step);
        }
      }
    }
  }

  public static void appendFromPlayerName(Document doc, Element game) {
    if (!playerName.isEmpty()) {
      Element playerList = doc.createElement("playerList");
      game.appendChild(playerList);
      playerList.appendChild(doc.createComment(" In Turn Order "));
      appendChildrenWithTwoAttributesFromList(doc, playerList, playerName, "player", "name", "optional", "false");

      final HashMap<String, ArrayList<String>> alliances = new HashMap<String, ArrayList<String>>();
      for (final Entry<String, String> allianceEntry : playerAlliance.entrySet()) {
        final String allianceName = allianceEntry.getValue();
        ArrayList<String> players = alliances.get(allianceName);
        if (players == null) {
          players = new ArrayList<String>();
          alliances.put(allianceName, players);
        }
        players.add(allianceEntry.getKey());
      }
      for (final Entry<String, ArrayList<String>> allianceEntry : alliances.entrySet()) {
        final String allianceName = allianceEntry.getKey();
        playerList.appendChild(doc.createComment(" " + allianceName + " Alliance "));
        appendChildrenWithTwoAttributesFromList(doc, playerList, allianceEntry.getValue(), "alliance", "player",
            "alliance", allianceName);
      }
    }
  }

  public static void appendFromUnitDefinitions(Document doc, Element game, Element map) {
    if (!unitDefinitions.isEmpty()) {
      map.appendChild(doc.createComment(" Unit Definitions "));
      final Element unitList = doc.createElement("unitList");
      game.appendChild(unitList);

      final Element production = doc.createElement("production");
      game.appendChild(production);
      final String firstResourceName = resourceList.get(0);
      for (final Entry<String, List<Integer>> unitDefinition : unitDefinitions.entrySet()) {
        final String unitName = unitDefinition.getKey();

        final Element unit = doc.createElement("unit");
        unit.setAttribute("name", unitName);
        unitList.appendChild(unit);

        final List<Integer> definition = unitDefinition.getValue();
        final Element productionRule = doc.createElement("productionRule");
        productionRule.setAttribute("name", "buy" + unitName.substring(0, 1).toUpperCase() + unitName.substring(1));
        production.appendChild(productionRule);
        final Element prCost = doc.createElement("cost");
        prCost.setAttribute("resource", firstResourceName);
        prCost.setAttribute("quantity", definition.get(0).toString());
        productionRule.appendChild(prCost);
        final Element prResult = doc.createElement("result");
        prResult.setAttribute("resourceOrUnit", unitName);
        prResult.setAttribute("quantity", definition.get(1).toString());
        productionRule.appendChild(prResult);
      }

      for (final Entry<String, List<String>> productionFrontierEntry : productionFrontiers.entrySet()) {
        final String productionFrontierName = productionFrontierEntry.getKey();

        final Element productionFrontier = doc.createElement("productionFrontier");
        productionFrontier.setAttribute("name", productionFrontierName);
        production.appendChild(productionFrontier);

        for (final String frontierRuleName : productionFrontierEntry.getValue()) {
          final Element frontierRule = doc.createElement("frontierRules");
          frontierRule.setAttribute("name", "buy" + frontierRuleName);
          productionFrontier.appendChild(frontierRule);
        }
      }
    }
  }

  /**
   * Appends new Elements to parentElement from list having two attributes.
   * Attribute 1 is coming from the provided list
   * Attribute 2 has a fix value for all new elements
   */
  public static void appendChildrenWithTwoAttributesFromList(final Document doc, final Element parentElement,
      final List<String> list, final String childName, final String attrOneKey, final String attrTwoKey,
      final String attrTwoFixValue) {
    for (final String attrOneValue : list) {
      final Element newChild = doc.createElement(childName);
      newChild.setAttribute(attrOneKey, attrOneValue);
      newChild.setAttribute(attrTwoKey, attrTwoFixValue);
      parentElement.appendChild(newChild);
    }
  }

  protected static void writeAttatchmentNodes(Document doc, final Element attatchmentList,
      final Map<String, List<String>> hashMap, final Element attatchmentTemplate) {
    final HashMap<String, List<Element>> playerAttatchOptions = new HashMap<String, List<Element>>();
    for (final Entry<String, List<String>> technologyDefinition : hashMap.entrySet()) {
      final List<String> definitionValues = technologyDefinition.getValue();
      final Element option = doc.createElement("option");
      final String techKey = technologyDefinition.getKey();
      option.setAttribute("name", techKey.substring(0, techKey.lastIndexOf(definitionValues.get(0)) - 1));
      option.setAttribute("value", definitionValues.get(1));
      final String playerName = definitionValues.get(0);
      List<Element> elementList = playerAttatchOptions.get(playerName);
      if (elementList == null) {
        elementList = new ArrayList<Element>();
        playerAttatchOptions.put(playerName, elementList);
      }
      elementList.add(option);
    }
    for (final Entry<String, List<Element>> optionElementList : playerAttatchOptions.entrySet()) {
      final String playerName = optionElementList.getKey();
      final Element attatchment = (Element) attatchmentTemplate.cloneNode(false);
      attatchment.setAttribute("attatchTo", playerName);
      attatchmentList.appendChild(attatchment);
      for (final Element option : optionElementList.getValue())
        attatchment.appendChild(option);
    }
  }

  public final static String playerNeutral = "<Neutral>";

  public static String[] getPlayersListInclNeutral() {
    playerName.add(playerNeutral);
    final String[] rVal = playerName.toArray(new String[playerName.size()]);
    playerName.remove(playerNeutral);
    return rVal;
  }

  /**
   * @param gbcToClone base GridBagConstraints object
   * @param gridx gridx value for new GridBagConstraints object
   * @param gridy gridy value for new GridBagConstraints object
   * @return cloned GridBagConstraints object with provided gridx and gridy values
   */
  public static GridBagConstraints getGBCCloneWith(final GridBagConstraints gbcToClone, final int gridx,
      final int gridy) {
    final GridBagConstraints gbcNew = (GridBagConstraints) gbcToClone.clone();
    gbcNew.gridx = gridx;
    gbcNew.gridy = gridy;
    return gbcNew;
  }

  /**
   * @param gbcToClone base GridBagConstraints object
   * @param gridx gridx value for new GridBagConstraints object
   * @param gridy gridy value for new GridBagConstraints object
   * @param anchor anchor value for new GridBagConstraints object
   * @return cloned GridBagConstraints object with provided gridx and gridy values
   */
  public static GridBagConstraints getGBCCloneWith(final GridBagConstraints gbcToClone, final int gridx,
      final int gridy, final int anchor) {
    final GridBagConstraints gbcNew = getGBCCloneWith(gbcToClone, gridx, gridy);
    gbcNew.anchor = anchor;
    return gbcNew;
  }
}