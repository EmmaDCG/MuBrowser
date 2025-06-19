package com.mu.game.model.stats;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class StatList2Client {
   public static final int firstLevelSortID = 1;
   public static final int RolePanel_1 = 1;
   public static final int RolePanel_2 = 2;
   private static List firstLevelSatList = new ArrayList();
   private static HashMap panelStatSort = new HashMap();
   private static HashMap panelSortStatMap = new HashMap();
   private static HashMap panelStats = new HashMap();
   public static List fightingStats = new ArrayList();
   public static List firstPanelStats = new ArrayList();
   public static List secondPanelStats = new ArrayList();
   public static List playerPanelStats = new ArrayList();
   public static List levelStats = new ArrayList();
   public static List teamerStats = new ArrayList();
   public static List monsterStats = new ArrayList();
   public static List petStats = new ArrayList();
   public static List aroundPlayerStats = new ArrayList();

   public static void init() throws Exception {
      List list = (List)panelSortStatMap.get(Integer.valueOf(1));
      if (list != null && list.size() >= 4) {
         firstLevelSatList.addAll(list);
         initFightingStats();
         initFirstPanelStats();
         initLevelStats();
         initSecondPanelStats();
         initTeamerStat();
         initPlayerPanelStats();
         initMosnterStats();
         initPetStats();
         initAroundPlayerStat();
      } else {
         throw new Exception("一级属性未填写或一级属性条数不足");
      }
   }

   public static void initXml(InputStream in) throws Exception {
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(in);
      Element root = doc.getRootElement();
      List childrean = root.getChildren("statSort");

      for(int i = 0; i < childrean.size(); ++i) {
         try {
            Element child = (Element)childrean.get(i);
            int sortId = child.getAttribute("id").getIntValue();
            String sortName = child.getAttributeValue("name");
            int panelId = child.getAttribute("panel").getIntValue();
            panelStatSort.put(sortId, sortName);
            List secondChildren = child.getChildren("statType");
            List statList = new ArrayList();

            for(int j = 0; j < secondChildren.size(); ++j) {
               Element typeChild = (Element)secondChildren.get(j);
               int statID = typeChild.getAttribute("id").getIntValue();
               StatEnum stat = StatEnum.find(statID);
               statList.remove(stat);
               statList.add(stat);
            }

            panelSortStatMap.put(sortId, statList);
            List panelList = (List)panelStats.get(panelId);
            if (panelList == null) {
               panelList = new ArrayList();
               panelStats.put(panelId, panelList);
            }

            ((List)panelList).addAll(statList);
         } catch (Exception var16) {
            var16.printStackTrace();
         }
      }

   }

   public static String getPanelStatName(int sortID) {
      String name = (String)panelStatSort.get(sortID);
      if (name == null) {
         name = "";
      }

      return name;
   }

   public static HashMap getPanelStatSort() {
      return panelStatSort;
   }

   public static HashMap getPanelStatMap() {
      return panelSortStatMap;
   }

   public static List getTeamerStats() {
      return teamerStats;
   }

   public static List getFightingStats() {
      return fightingStats;
   }

   public static List getFirstPanelStats() {
      return firstPanelStats;
   }

   public static List getSecondPanelStats() {
      return secondPanelStats;
   }

   public static List getPlayerPanelStats() {
      return playerPanelStats;
   }

   public static List getLevelStats() {
      return levelStats;
   }

   public static List getMonsterStats() {
      return monsterStats;
   }

   public static List getAroundPlayerStats() {
      return aroundPlayerStats;
   }

   public static List getFirstLevelSatList() {
      return firstLevelSatList;
   }

   private static void initTeamerStat() {
      teamerStats.add(StatEnum.HP);
      teamerStats.add(StatEnum.MAX_HP);
      teamerStats.add(StatEnum.MP);
      teamerStats.add(StatEnum.MAX_MP);
      teamerStats.add(StatEnum.LEVEL);
   }

   private static void initAroundPlayerStat() {
      aroundPlayerStats.add(StatEnum.HP);
      aroundPlayerStats.add(StatEnum.MAX_HP);
      aroundPlayerStats.add(StatEnum.MP);
      aroundPlayerStats.add(StatEnum.MAX_MP);
      aroundPlayerStats.add(StatEnum.LEVEL);
      aroundPlayerStats.add(StatEnum.SPEED);
   }

   private static void initFightingStats() {
      List list = (List)panelStats.get(Integer.valueOf(1));
      if (list != null) {
         fightingStats.addAll(list);
      }

      fightingStats.add(StatEnum.HP);
      fightingStats.add(StatEnum.MP);
   }

   private static void initFirstPanelStats() {
      List list = (List)panelStats.get(Integer.valueOf(1));
      if (list != null) {
         firstPanelStats.addAll(list);
      }

      firstPanelStats.add(StatEnum.HP);
      firstPanelStats.add(StatEnum.MP);
   }

   private static void initSecondPanelStats() {
      List list = (List)panelStats.get(Integer.valueOf(2));
      if (list != null) {
         secondPanelStats.addAll(list);
      }

   }

   private static void initPlayerPanelStats() {
      Iterator var1 = panelSortStatMap.values().iterator();

      while(var1.hasNext()) {
         List statSet = (List)var1.next();
         playerPanelStats.addAll(statSet);
      }

      playerPanelStats.add(StatEnum.SPEED);
      playerPanelStats.add(StatEnum.MONEY);
      playerPanelStats.add(StatEnum.INGOT);
      playerPanelStats.add(StatEnum.BIND_INGOT);
      playerPanelStats.add(StatEnum.LEVEL);
      playerPanelStats.add(StatEnum.POTENTIAL);
      playerPanelStats.add(StatEnum.EXP);
      playerPanelStats.add(StatEnum.MAX_EXP);
      playerPanelStats.add(StatEnum.AP);
      playerPanelStats.add(StatEnum.MAX_AP);
      playerPanelStats.add(StatEnum.HP);
      playerPanelStats.add(StatEnum.MP);
      playerPanelStats.add(StatEnum.SD);
      playerPanelStats.add(StatEnum.AG);
      playerPanelStats.add(StatEnum.MAX_AG);
      playerPanelStats.add(StatEnum.REDEEM_POINTS);
      playerPanelStats.add(StatEnum.All_Points);
      playerPanelStats.add(StatEnum.Contribution);
      playerPanelStats.add(StatEnum.HisContribution);
      playerPanelStats.add(StatEnum.DOMINEERING);
   }

   private static void initLevelStats() {
      List list = (List)panelStats.get(Integer.valueOf(1));
      if (list != null) {
         levelStats.addAll(list);
      }

      levelStats.add(StatEnum.HP);
      levelStats.add(StatEnum.MP);
   }

   private static void initMosnterStats() {
      monsterStats.add(StatEnum.HP);
      monsterStats.add(StatEnum.MAX_HP);
      monsterStats.add(StatEnum.MP);
      monsterStats.add(StatEnum.MAX_MP);
      monsterStats.add(StatEnum.SD);
      monsterStats.add(StatEnum.MAX_SD);
      monsterStats.add(StatEnum.ATK_MIN);
      monsterStats.add(StatEnum.ATK_MAX);
      monsterStats.add(StatEnum.DEF);
      monsterStats.add(StatEnum.HIT);
      monsterStats.add(StatEnum.AVD);
      monsterStats.add(StatEnum.LEVEL);
      monsterStats.add(StatEnum.SPEED);
   }

   public static List getPetStats() {
      return petStats;
   }

   private static void initPetStats() {
      petStats.add(StatEnum.LEVEL);
      petStats.add(StatEnum.HP);
      petStats.add(StatEnum.MAX_HP);
      petStats.add(StatEnum.SPEED);
   }

   public static List getOtherPlayerStats() {
      List otherStats = new ArrayList();
      otherStats.add(StatEnum.MAX_HP);
      otherStats.add(StatEnum.MAX_MP);
      otherStats.add(StatEnum.ATK_MIN);
      otherStats.add(StatEnum.ATK_MAX);
      otherStats.add(StatEnum.DEF);
      otherStats.add(StatEnum.HIT);
      otherStats.add(StatEnum.AVD);
      otherStats.add(StatEnum.ATK_SPEED);
      otherStats.add(StatEnum.DOMINEERING);
      return otherStats;
   }

   public static List getDieOrRevivalStats() {
      List drList = new ArrayList();
      drList.add(StatEnum.HP);
      drList.add(StatEnum.MAX_HP);
      drList.add(StatEnum.MP);
      drList.add(StatEnum.MAX_MP);
      drList.add(StatEnum.SD);
      drList.add(StatEnum.MAX_SD);
      drList.add(StatEnum.SPEED);
      return drList;
   }
}
