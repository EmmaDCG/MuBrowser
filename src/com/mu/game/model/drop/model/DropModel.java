package com.mu.game.model.drop.model;

import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.weight.WeightAtom;
import com.mu.utils.Rnd;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class DropModel extends WeightAtom {
   private static HashMap models = new HashMap();
   private int groupID;
   private int dropID;
   private int modelID;
   private int minCount;
   private int maxCount;
   private int minRoleLevel;
   private int maxRoleLevel;
   private int personMaxCountPerDay = -1;
   private int serverMaxCountPerDay = -1;
   private int timeRange = -1;

   public DropModel(int groupID, int dropID, int modelID, int weight, int minCount, int maxCount, int minRoleLevel, int maxRoleLevel, int personMaxCountPerDay, int serverMaxCountPerDay, int timeRange) {
      super(weight);
      this.groupID = groupID;
      this.dropID = dropID;
      this.modelID = modelID;
      this.minCount = minCount;
      this.maxCount = maxCount;
      this.minRoleLevel = minRoleLevel;
      this.maxRoleLevel = maxRoleLevel;
      this.personMaxCountPerDay = personMaxCountPerDay;
      this.serverMaxCountPerDay = serverMaxCountPerDay;
      this.timeRange = timeRange;
   }

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet modelSheet = wb.getSheet(1);
      init(modelSheet);
      Sheet rateControlDrop = wb.getSheet(2);
      DropControlManager.initRateControl(rateControlDrop);
      WellDropManager.init(wb.getSheet(3));
   }

   private static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int groupID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int dropID = Tools.getCellIntValue(sheet.getCell("B" + i));
         int modelID = Tools.getCellIntValue(sheet.getCell("C" + i));
         int weight = Tools.getCellIntValue(sheet.getCell("D" + i));
         int minCount = Tools.getCellIntValue(sheet.getCell("E" + i));
         int maxCount = Tools.getCellIntValue(sheet.getCell("F" + i));
         int minRoleLevel = Tools.getCellIntValue(sheet.getCell("G" + i));
         int maxRoleLevel = Tools.getCellIntValue(sheet.getCell("H" + i));
         int personMaxCountPerDay = Tools.getCellIntValue(sheet.getCell("I" + i));
         int serverMaxCountPerDay = Tools.getCellIntValue(sheet.getCell("J" + i));
         String timeStr = sheet.getCell("K" + i).getContents();
         int timeRange = getAllDayTimeRange();
         if (timeStr != null && timeStr.length() > 0) {
            timeRange = anysizeTime(timeStr, dropID);
         }

         if (modelID != -1 && ItemModel.getModel(modelID) == null) {
            throw new Exception("掉落模板，物品不存在，第 " + i + "行");
         }

         if (weight < 0) {
            throw new Exception("掉落模板，权重数值不合适 ，掉落ID = " + dropID);
         }

         int tmpCount;
         if (minRoleLevel > maxRoleLevel) {
            tmpCount = maxRoleLevel;
            maxRoleLevel = minRoleLevel;
            minRoleLevel = tmpCount;
         }

         if (minCount > maxCount) {
            tmpCount = minCount;
            minCount = maxCount;
            maxCount = tmpCount;
         }

         if (minCount < 1) {
            throw new Exception("掉落模板，数量不正确，掉落ID = " + dropID);
         }

         if (minRoleLevel < 1) {
            throw new Exception("掉落模板，最小人物等级不正确，掉落ID = " + dropID);
         }

         DropModel model = new DropModel(groupID, dropID, modelID, weight, minCount, maxCount, minRoleLevel, maxRoleLevel, personMaxCountPerDay, serverMaxCountPerDay, timeRange);
         models.put(model.getDropID(), model);
         DropGroup group = DropGroup.getGroup(groupID);
         if (group == null) {
            group = new DropGroup(groupID);
            DropGroup.addGroup(group);
         }

         group.addDropModel(model);
      }

      DropGroup.checkAll();
   }

   public boolean canDrop(int roleLevel) {
      return roleLevel >= this.minRoleLevel;
   }

   public int getCount(int personDay, int serverDay) {
      if (this.serverMaxCountPerDay > 0 && serverDay >= this.serverMaxCountPerDay) {
         return 0;
      } else if (this.personMaxCountPerDay > 0 && personDay >= this.personMaxCountPerDay) {
         return 0;
      } else {
         int tmpCount = Rnd.get(this.minCount, this.maxCount);
         if (this.personMaxCountPerDay > 0 && personDay + tmpCount > this.personMaxCountPerDay) {
            tmpCount = this.personMaxCountPerDay - personDay;
         }

         if (this.serverMaxCountPerDay > 0 && serverDay + tmpCount > this.serverMaxCountPerDay) {
            tmpCount = this.serverMaxCountPerDay - serverDay;
         }

         return tmpCount;
      }
   }

   public static DropModel getModel(int dropID) {
      return (DropModel)models.get(dropID);
   }

   private static int anysizeTime(String timeStr, int dropID) throws Exception {
      String[] splits = timeStr.split("-");
      if (splits.length < 2) {
         throw new Exception("掉落模板，时间 出错  ,掉落ID = " + dropID);
      } else {
         String[] starts = splits[0].split(":");
         if (starts.length < 2) {
            throw new Exception("掉落模板，开始时间出错，掉落ID = " + dropID);
         } else {
            String[] ends = splits[1].split(":");
            if (ends.length < 2) {
               throw new Exception("掉落模板，结束时间出错 ，掉落ID = " + dropID);
            } else {
               int sh = Integer.parseInt(starts[0]);
               int sm = Integer.parseInt(starts[1]);
               int eh = Integer.parseInt(ends[0]);
               int em = Integer.parseInt(ends[1]);
               return getTimeRange(sh, sm, eh, em);
            }
         }
      }
   }

   public ItemDataUnit createUnit(int count, boolean isBind, int statRuleID) {
      ItemDataUnit unit = new ItemDataUnit(this.modelID, count, isBind);
      unit.setSource(2);
      unit.setStatRuleID(statRuleID);
      unit.setHide(false);
      return unit;
   }

   private static int getAllDayTimeRange() {
      int sh = 0;
      int sm = 0;
      int eh = 24;
      int em = 0;
      int result = sh << 24 | sm << 16 | eh << 8 | em;
      return result;
   }

   private static int getTimeRange(int sh, int sm, int eh, int em) {
      if (sh < 0) {
         sh = 0;
      }

      if (eh > 24) {
         eh = 24;
      }

      if (eh == 24) {
         em = 0;
      }

      int tmpM;
      if (sh > eh) {
         tmpM = sh;
         sh = eh;
         eh = tmpM;
      } else if (sh == eh && sm > em) {
         tmpM = em;
         em = sm;
         sm = tmpM;
      }

      if (sh < 0) {
         sh = 0;
      }

      if (eh > 24) {
         eh = 24;
      }

      if (eh == 24) {
         em = 0;
      }

      return sh << 24 | sm << 16 | eh << 8 | em;
   }

   public int getGroupID() {
      return this.groupID;
   }

   public void setGroupID(int groupID) {
      this.groupID = groupID;
   }

   public int getDropID() {
      return this.dropID;
   }

   public void setDropID(int dropID) {
      this.dropID = dropID;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getMinCount() {
      return this.minCount;
   }

   public void setMinCount(int minCount) {
      this.minCount = minCount;
   }

   public int getMaxCount() {
      return this.maxCount;
   }

   public void setMaxCount(int maxCount) {
      this.maxCount = maxCount;
   }

   public int getMinRoleLevel() {
      return this.minRoleLevel;
   }

   public void setMinRoleLevel(int minRoleLevel) {
      this.minRoleLevel = minRoleLevel;
   }

   public int getMaxRoleLevel() {
      return this.maxRoleLevel;
   }

   public void setMaxRoleLevel(int maxRoleLevel) {
      this.maxRoleLevel = maxRoleLevel;
   }

   public int getTimeRange() {
      return this.timeRange;
   }

   public void setTimeRange(int timeRange) {
      this.timeRange = timeRange;
   }

   public int getPersonMaxCountPerDay() {
      return this.personMaxCountPerDay;
   }

   public void setPersonMaxCountPerDay(int personMaxCountPerDay) {
      this.personMaxCountPerDay = personMaxCountPerDay;
   }

   public int getServerMaxCountPerDay() {
      return this.serverMaxCountPerDay;
   }

   public void setServerMaxCountPerDay(int serverMaxCountPerDay) {
      this.serverMaxCountPerDay = serverMaxCountPerDay;
   }
}
