package com.mu.game.model.equip.compositenew;

import com.mu.game.model.equip.compositenew.condition.MaterialConFactory;
import com.mu.game.model.equip.compositenew.condition.MaterialCondition;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.model.ItemSort;
import com.mu.game.model.service.StringTools;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;

public class MaterialModel {
   public static int MaterialType_1 = 1;
   public static int MaterialType_2 = 2;
   private static HashMap materialMap = new HashMap();
   private int materialID;
   private int type;
   private HashSet optionalIDs = null;
   private int count;
   private String gridTitle;
   private String gridContent;
   private boolean need;
   private boolean failConsume;
   private boolean auto;
   private List conditionList = null;
   private int rateType;
   private Item showItem = null;

   public MaterialModel(int materialID) {
      this.materialID = materialID;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int materialID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int type = Tools.getCellIntValue(sheet.getCell("B" + i));
         String typeStr = Tools.getCellValue(sheet.getCell("C" + i));
         int count = Tools.getCellIntValue(sheet.getCell("D" + i));
         String gridTitle = Tools.getCellValue(sheet.getCell("E" + i));
         String gridContent = Tools.getCellValue(sheet.getCell("F" + i));
         boolean need = Tools.getCellIntValue(sheet.getCell("G" + i)) == 1;
         boolean failConsume = Tools.getCellIntValue(sheet.getCell("H" + i)) == 1;
         boolean auto = Tools.getCellIntValue(sheet.getCell("I" + i)) == 1;
         String conStr = Tools.getCellValue(sheet.getCell("J" + i));
         int rateType = Tools.getCellIntValue(sheet.getCell("K" + i));
         HashSet optionalIDs = StringTools.analyzeIntegerHashset(typeStr, ",");
         if (optionalIDs.size() < 1) {
            throw new Exception(sheet.getName() + "--可放入的物品ID 填写错误，第 " + i + "行");
         }

         Iterator var16 = optionalIDs.iterator();

         while(var16.hasNext()) {
            Integer modelID = (Integer)var16.next();
            if (type == MaterialType_1) {
               ItemModel model = ItemModel.getModel(modelID.intValue());
               if (model == null) {
                  throw new Exception(sheet.getName() + "--可放入的物品ID-模板不存在 填写错误，第 " + i + "行");
               }
            } else if (!ItemSort.hasItemType(modelID.intValue())) {
               throw new Exception(sheet.getName() + "--可放入的物品ID-道具小类不存在 填写错误，第 " + i + "行");
            }
         }

         if (count < 1) {
            throw new Exception(sheet.getName() + "--个数 填写错误，第 " + i + "行");
         }

         if (gridTitle == null) {
            gridTitle = "";
         }

         if (gridContent == null) {
            gridContent = "";
         }

         Item item = null;
         if (auto) {
            if (!need) {
               throw new Exception(sheet.getName() + "--自动放入的材料一定是必须的材料，第 " + i + "行");
            }

            if (type != MaterialType_1) {
               throw new Exception(sheet.getName() + "--自动放入必须是道具类型，第 " + i + "行");
            }

            if (optionalIDs.size() > 1) {
               throw new Exception(sheet.getName() + "--自动放入  （可放入的物品ID）填写错误，第 " + i + "行");
            }

            Iterator var24 = optionalIDs.iterator();
            if (var24.hasNext()) {
               Integer modelID = (Integer)var24.next();
               item = ItemTools.createItem(modelID.intValue(), count, 2);
               if (item == null) {
                  throw new Exception(sheet.getName() + "物品不存在，第 " + i + "行");
               }
            }
         }

         HashSet conSet = StringTools.analyzeIntegerHashset(conStr, ",");
         List conList = new ArrayList();
         Iterator var19 = conSet.iterator();

         while(var19.hasNext()) {
            Integer conID = (Integer)var19.next();
            MaterialCondition condition = MaterialConFactory.getCondition(conID.intValue());
            if (condition == null) {
               throw new Exception(sheet.getName() + "--条件填写错误 填写错误，第 " + i + "行");
            }

            conList.add(condition);
         }

         MaterialModel model = new MaterialModel(materialID);
         model.setType(type);
         model.setOptionalIDs(optionalIDs);
         model.setCount(count);
         model.setGridTitle(gridTitle);
         model.setGridContent(gridContent);
         model.setNeed(need);
         model.setFailConsume(failConsume);
         model.setAuto(auto);
         model.setConditionList(conList);
         model.setRateType(rateType);
         model.setShowItem(item);
         materialMap.put(materialID, model);
      }

   }

   public static MaterialModel getMaterialModel(int materialID) {
      return (MaterialModel)materialMap.get(materialID);
   }

   public boolean suit(Item item) {
      if (this.getType() == MaterialType_1) {
         if (!this.getOptionalIDs().contains(item.getModelID())) {
            return false;
         }
      } else if (!this.getOptionalIDs().contains(item.getItemType())) {
         return false;
      }

      Iterator var3 = this.getConditionList().iterator();

      while(var3.hasNext()) {
         MaterialCondition condition = (MaterialCondition)var3.next();
         if (!condition.suit(item)) {
            return false;
         }
      }

      return true;
   }

   public int getMaterialID() {
      return this.materialID;
   }

   public void setMaterialID(int materialID) {
      this.materialID = materialID;
   }

   public boolean isFailConsume() {
      return this.failConsume;
   }

   public void setFailConsume(boolean failConsume) {
      this.failConsume = failConsume;
   }

   public List getConditionList() {
      return this.conditionList;
   }

   public void setConditionList(List conditionList) {
      this.conditionList = conditionList;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public HashSet getOptionalIDs() {
      return this.optionalIDs;
   }

   public void setOptionalIDs(HashSet optionalIDs) {
      this.optionalIDs = optionalIDs;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public String getGridTitle() {
      return this.gridTitle;
   }

   public void setGridTitle(String gridTitle) {
      this.gridTitle = gridTitle;
   }

   public String getGridContent() {
      return this.gridContent;
   }

   public void setGridContent(String gridContent) {
      this.gridContent = gridContent;
   }

   public boolean isNeed() {
      return this.need;
   }

   public void setNeed(boolean need) {
      this.need = need;
   }

   public boolean isAuto() {
      return this.auto;
   }

   public void setAuto(boolean auto) {
      this.auto = auto;
   }

   public int getRateType() {
      return this.rateType;
   }

   public void setRateType(int rateType) {
      this.rateType = rateType;
   }

   public Item getShowItem() {
      return this.showItem;
   }

   public void setShowItem(Item showItem) {
      this.showItem = showItem;
   }
}
