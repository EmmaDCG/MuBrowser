package com.mu.game.model.unit.buff.model;

import com.mu.game.model.service.StringTools;
import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;

public class BuffGroup {
   private static HashMap buffToGroups = new HashMap();
   private static HashMap groups = new HashMap();
   private int groupID;
   private String groupName;
   private boolean metux;
   private HashMap buffs = null;
   private boolean needToPop;
   private String popTitle;
   private String seniorPopStr;
   private String lowerPopStr;

   public BuffGroup(int groupID, String groupName, boolean metux) {
      this.groupID = groupID;
      this.groupName = groupName;
      this.metux = metux;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         String groupName = Tools.getCellValue(sheet.getCell("A" + i));
         int groupID = Tools.getCellIntValue(sheet.getCell("B" + i));
         boolean metux = Tools.getCellIntValue(sheet.getCell("C" + i)) == 1;
         String buffStr = Tools.getCellValue(sheet.getCell("D" + i));
         HashMap buffs = StringTools.analyzeIntegerMap(buffStr, ",");
         boolean needToPop = Tools.getCellIntValue(sheet.getCell("E" + i)) == 1;
         String popTitle = Tools.getCellValue(sheet.getCell("F" + i));
         String seniorPopStr = Tools.getCellValue(sheet.getCell("G" + i));
         String lowerPopStr = Tools.getCellValue(sheet.getCell("H" + i));
         Iterator var13 = buffs.keySet().iterator();

         while(var13.hasNext()) {
            Integer buffID = (Integer)var13.next();
            if (!BuffModel.hasModel(buffID.intValue())) {
               throw new Exception("buff组 -- buffID 不存在 ，组ID = " + groupID);
            }

            if (buffToGroups.containsKey(buffID)) {
               throw new Exception("buff组 -- buffID 存在于两个以上的组 ，组ID = " + groupID + ", buffID = " + buffID);
            }

            buffToGroups.put(buffID, groupID);
         }

         BuffGroup bg = new BuffGroup(groupID, groupName, metux);
         bg.setBuffs(buffs);
         bg.setSeniorPopStr(seniorPopStr);
         bg.setLowerPopStr(lowerPopStr);
         bg.setNeedToPop(needToPop);
         bg.setPopTitle(popTitle);
         groups.put(groupID, bg);
      }

   }

   public static BuffGroup getBuffGroup(int buffID) {
      if (buffToGroups.containsKey(buffID)) {
         int groupID = ((Integer)buffToGroups.get(buffID)).intValue();
         return (BuffGroup)groups.get(groupID);
      } else {
         return null;
      }
   }

   public boolean isPriority(int newBuffID, int oldBuffID) {
      Integer priorityNew = (Integer)this.buffs.get(newBuffID);
      if (priorityNew == null) {
         return false;
      } else {
         Integer priorityOld = (Integer)this.buffs.get(oldBuffID);
         if (priorityOld == null) {
            return true;
         } else {
            return priorityNew.intValue() > priorityOld.intValue();
         }
      }
   }

   public int getGroupID() {
      return this.groupID;
   }

   public void setGroupID(int groupID) {
      this.groupID = groupID;
   }

   public boolean isMetux() {
      return this.metux;
   }

   public void setMetux(boolean metux) {
      this.metux = metux;
   }

   public HashMap getBuffs() {
      return this.buffs;
   }

   public void setBuffs(HashMap buffs) {
      this.buffs = buffs;
   }

   public String getGroupName() {
      return this.groupName;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public boolean isNeedToPop() {
      return this.needToPop;
   }

   public void setNeedToPop(boolean needToPop) {
      this.needToPop = needToPop;
   }

   public String getSeniorPopStr() {
      return this.seniorPopStr;
   }

   public void setSeniorPopStr(String seniorPopStr) {
      this.seniorPopStr = seniorPopStr;
   }

   public String getLowerPopStr() {
      return this.lowerPopStr;
   }

   public void setLowerPopStr(String lowerPopStr) {
      this.lowerPopStr = lowerPopStr;
   }

   public String getPopTitle() {
      return this.popTitle;
   }

   public void setPopTitle(String popTitle) {
      this.popTitle = popTitle;
   }
}
