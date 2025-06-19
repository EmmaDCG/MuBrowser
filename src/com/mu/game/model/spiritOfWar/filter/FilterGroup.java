package com.mu.game.model.spiritOfWar.filter;

import com.mu.game.model.service.StringTools;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.Map.Entry;
import jxl.Sheet;

public class FilterGroup {
   private static HashMap groupMap = new HashMap();
   private static List groupList = new ArrayList();
   private int groupID;
   private String prefixName;
   private String suxName;
   private List conditionList;
   private int defaultIndex = 0;

   public FilterGroup(int groupID, String prefixName, String suxName, List conditionList) {
      this.groupID = groupID;
      this.prefixName = prefixName;
      this.suxName = suxName;
      this.conditionList = conditionList;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int groupID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String prefixName = Tools.getCellValue(sheet.getCell("B" + i));
         String conStr = Tools.getCellValue(sheet.getCell("C" + i));
         int defaultConID = Tools.getCellIntValue(sheet.getCell("D" + i));
         if (groupMap.containsKey(groupID)) {
            throw new Exception("积分兑换 -" + sheet.getName() + "-填写的groupID重复,第" + i + "行");
         }

         List conList = StringTools.analyzeIntegerList(conStr, ",");
         if (conList.size() < 1) {
            throw new Exception("积分兑换 -" + sheet.getName() + "-填写的筛选ID集合为空,第" + i + "行");
         }

         List conditionList = new ArrayList();

         int defaultIndex;
         for(defaultIndex = 0; defaultIndex < conList.size(); ++defaultIndex) {
            FilterCondition condition = FilterCondition.getfilterCondition(((Integer)conList.get(defaultIndex)).intValue());
            if (condition == null) {
               throw new Exception("积分兑换 -" + sheet.getName() + "-填写的筛选组合ID不存在,第" + i + "行");
            }

            conditionList.add(condition);
         }

         defaultIndex = -1;

         for(int index = 0; index < conditionList.size(); ++index) {
            if (((FilterCondition)conditionList.get(index)).getId() == defaultConID) {
               defaultIndex = index;
               break;
            }
         }

         if (FilterCondition.getfilterCondition(defaultConID) == null || defaultIndex == -1) {
            throw new Exception("积分兑换-" + sheet.getName() + "-默认条件不存在");
         }

         FilterGroup group = new FilterGroup(groupID, prefixName, "", conditionList);
         group.setDefaultIndex(defaultIndex);
         if (groupMap.containsKey(groupID)) {
            throw new Exception("积分兑换-" + sheet.getName() + "ID重复，ID = " + groupID);
         }

         groupMap.put(group.getGroupID(), group);
         groupList.add(group);
      }

   }

   public static void checkPlayerCondition(SortedMap conditions) {
      Iterator it = conditions.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         FilterGroup group = getFilterGroup(((Integer)entry.getKey()).intValue());
         if (group == null) {
            it.remove();
         } else if (getFilterGroupByIndex(((Integer)entry.getValue()).intValue()) == null) {
            entry.setValue(group.getDefaultIndex());
         }
      }

      Iterator var5 = groupList.iterator();

      while(var5.hasNext()) {
         FilterGroup group = (FilterGroup)var5.next();
         if (!conditions.containsKey(group.getGroupID())) {
            conditions.put(group.getGroupID(), group.getDefaultIndex());
         }
      }

   }

   public static FilterGroup getFilterGroupByIndex(int index) {
      return index >= 0 && index < groupList.size() ? (FilterGroup)groupList.get(index) : null;
   }

   public static FilterGroup getFilterGroup(int groupID) {
      return (FilterGroup)groupMap.get(groupID);
   }

   public static List getGroupList() {
      return groupList;
   }

   public FilterCondition getFilterCondition(int index) {
      return index >= 0 && index < this.conditionList.size() ? (FilterCondition)this.conditionList.get(index) : null;
   }

   public int getDefaultIndex() {
      return this.defaultIndex;
   }

   public void setDefaultIndex(int defaultIndex) {
      this.defaultIndex = defaultIndex;
   }

   public int getGroupID() {
      return this.groupID;
   }

   public void setGroupID(int groupID) {
      this.groupID = groupID;
   }

   public String getPrefixName() {
      return this.prefixName;
   }

   public void setPrefixName(String prefixName) {
      this.prefixName = prefixName;
   }

   public String getSuxName() {
      return this.suxName;
   }

   public void setSuxName(String suxName) {
      this.suxName = suxName;
   }

   public List getConditionList() {
      return this.conditionList;
   }
}
