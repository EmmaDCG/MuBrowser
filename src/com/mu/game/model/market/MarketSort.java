package com.mu.game.model.market;

import com.mu.game.model.item.Item;
import com.mu.game.model.market.condition.MarketCondition;
import com.mu.game.model.service.StringTools;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;

public class MarketSort {
   private static HashMap sortMap = new HashMap();
   private static HashMap firstSortMap = new HashMap();
   private static HashSet canSellItemTypes = new HashSet();
   private int sortID;
   private String name;
   private int fatherSortID;
   private int index;
   private List sonSortIDList = new ArrayList();
   private HashSet professionSet = new HashSet();
   private HashSet itemTypes = new HashSet();
   private List conditions = new ArrayList();

   public MarketSort(int sortID, String name, int fatherSortID) {
      this.sortID = sortID;
      this.name = name;
      this.fatherSortID = fatherSortID;
   }

   public boolean addCheck(Item item) {
      if (!this.itemTypes.contains(Integer.valueOf(-1)) && !this.itemTypes.contains(item.getItemType())) {
         return false;
      } else {
         HashSet itemProfession = item.getModel().getProfession();
         if (itemProfession.size() < 1) {
            return true;
         } else if (this.professionSet.contains(Integer.valueOf(-1))) {
            return true;
         } else {
            Iterator var4 = itemProfession.iterator();

            while(var4.hasNext()) {
               Integer itemPro = (Integer)var4.next();
               if (this.professionSet.contains(itemPro)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int sortID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int fatherSortID = Tools.getCellIntValue(sheet.getCell("C" + i));
         String proStr = Tools.getCellValue(sheet.getCell("D" + i));
         HashSet itemTypes = StringTools.analyzeIntegerHashset(Tools.getCellValue(sheet.getCell("E" + i)), ",");
         HashSet professionSet = StringTools.analyzeIntegerHashset(proStr, ",");
         List conditionList = StringTools.analyzeIntegerList(Tools.getCellValue(sheet.getCell("F" + i)), ",");
         int index = Tools.getCellIntValue(sheet.getCell("G" + i));
         MarketSort ms = new MarketSort(sortID, name, fatherSortID);
         ms.setItemTypes(itemTypes);
         ms.setProfessionSet(professionSet);
         Iterator var13 = conditionList.iterator();

         while(var13.hasNext()) {
            Integer conditionID = (Integer)var13.next();
            MarketCondition condition = MarketCondition.getCondition(conditionID.intValue());
            if (condition == null) {
               throw new Exception("市场-" + sheet.getName() + "筛选条件找不到,第" + i);
            }

            ms.getConditions().add(condition);
         }

         ms.setIndex(index);
         addMarketSort(ms, "市场 - " + sheet.getName() + "-第 " + i + " 行");
      }

   }

   private static void addMarketSort(MarketSort sort, String des) throws Exception {
      if (sortMap.containsKey(sort.getSortID())) {
         throw new Exception(des + "-ID重复");
      } else {
         sortMap.put(sort.getSortID(), sort);
         if (sort.getFatherSortID() == -1) {
            firstSortMap.put(sort.getSortID(), sort);
         } else {
            MarketSort fatherSort = getSort(sort.getFatherSortID());
            if (fatherSort == null) {
               throw new Exception(des + "：没有父ID");
            }

            fatherSort.getSonSortIDList().add(sort.getSortID());
         }

         canSellItemTypes.addAll(sort.getItemTypes());
      }
   }

   public static MarketSort getSort(int sortID) {
      return (MarketSort)sortMap.get(sortID);
   }

   public boolean hasSonSort(int sonSortID) {
      return this.sonSortIDList.contains(sonSortID);
   }

   public MarketCondition getCondition(int index) {
      return index >= 0 && index < this.conditions.size() ? (MarketCondition)this.getConditions().get(index) : null;
   }

   public int getSortID() {
      return this.sortID;
   }

   public void setSortID(int sortID) {
      this.sortID = sortID;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getFatherSortID() {
      return this.fatherSortID;
   }

   public void setFatherSortID(int fatherSortID) {
      this.fatherSortID = fatherSortID;
   }

   public HashSet getProfessionSet() {
      return this.professionSet;
   }

   public void setProfessionSet(HashSet professionSet) {
      this.professionSet = professionSet;
   }

   public HashSet getItemTypes() {
      return this.itemTypes;
   }

   public void setItemTypes(HashSet itemTypes) {
      this.itemTypes = itemTypes;
   }

   public List getConditions() {
      return this.conditions;
   }

   public void setConditions(List conditions) {
      this.conditions = conditions;
   }

   public static HashMap getSortMap() {
      return sortMap;
   }

   public List getSonSortIDList() {
      return this.sonSortIDList;
   }

   public void setSonSortIDList(List sonSortIDList) {
      this.sonSortIDList = sonSortIDList;
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }

   public static HashMap getFirstSortMap() {
      return firstSortMap;
   }

   public static HashSet getCanSellItemTypes() {
      return canSellItemTypes;
   }
}
