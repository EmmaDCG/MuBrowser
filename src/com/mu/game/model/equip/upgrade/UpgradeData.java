package com.mu.game.model.equip.upgrade;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;

public class UpgradeData {
   private static HashMap dataMap = new HashMap();
   private int modelID;
   private int taregtID;
   private HashMap needItems;
   private int money;
   private int rate;
   private int ingot;
   private List showItems = null;

   public UpgradeData(int modelID, int taregtID, HashMap needItems, int money, int rate) {
      this.modelID = modelID;
      this.taregtID = taregtID;
      this.needItems = needItems;
      this.money = money;
      this.rate = rate;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int modelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int targetID = Tools.getCellIntValue(sheet.getCell("B" + i));
         String needStr = Tools.getCellValue(sheet.getCell("C" + i));
         int money = Tools.getCellIntValue(sheet.getCell("D" + i));
         int rate = Tools.getCellIntValue(sheet.getCell("E" + i));
         int ingot = Tools.getCellIntValue(sheet.getCell("F" + i));
         String des = "，第 " + i + " 行";
         if (!ItemModel.hasItemModel(modelID)) {
            throw new Exception(sheet.getName() + "-装备不存在 " + des);
         }

         if (!ItemModel.hasItemModel(targetID)) {
            throw new Exception(sheet.getName() + "- 目标装备不存在 " + des);
         }

         if (money < 0) {
            throw new Exception(sheet.getName() + " - 金钱错误 " + des);
         }

         if (rate < 0) {
            throw new Exception(sheet.getName() + " - 概率不正确" + des);
         }

         if (ingot < 1) {
            throw new Exception(sheet.getName() + " - 钻石数量不正确");
         }

         List showItems = new ArrayList();
         HashMap needItem = new HashMap();
         String[] fSpits = needStr.split(";");
         String[] var16 = fSpits;
         int var15 = fSpits.length;

         for(int var14 = 0; var14 < var15; ++var14) {
            String s = var16[var14];
            String[] sSpits = s.split(",");
            int itemModelID = Integer.parseInt(sSpits[0]);
            int count = Integer.parseInt(sSpits[1]);
            if (!ItemModel.hasItemModel(itemModelID)) {
               throw new Exception(sheet.getName() + "- 所需道具ID不存在" + des);
            }

            if (count < 1 || count > ItemModel.getModel(itemModelID).getMaxStackCount()) {
               throw new Exception(sheet.getName() + "- 所需道具 数量不正确" + des);
            }

            Item item = ItemTools.createItem(itemModelID, count, 2);
            if (item == null) {
               throw new Exception(sheet.getName() + "- 所需道具ID不存在" + des);
            }

            showItems.add(item);
            needItem.put(itemModelID, count);
         }

         if (needItem.size() < 1) {
            throw new Exception(sheet.getName() + " - 没有填写升级所需的道具" + des);
         }

         if (!ItemModel.getModel(modelID).isEquipment()) {
            throw new Exception(sheet.getName() + " - 升级道具不是装备 " + des);
         }

         if (!ItemModel.getModel(targetID).isEquipment()) {
            throw new Exception(sheet.getName() + " - 目标道具不是装备 " + des);
         }

         if (modelID == targetID) {
            throw new Exception(sheet.getName() + "-升级装备 和 目标装备 相同" + des);
         }

         if (ItemModel.getModel(modelID).getItemType() != ItemModel.getModel(targetID).getItemType()) {
            throw new Exception(sheet.getName() + " - 升级装备和目标装备不是相同类型的道具" + des);
         }

         UpgradeData data = new UpgradeData(modelID, targetID, needItem, money, rate);
         data.setIngot(ingot);
         data.setShowItems(showItems);
         dataMap.put(data.getModelID(), data);
      }

   }

   public static boolean hasUpgrade(int modelID) {
      return dataMap.containsKey(modelID);
   }

   public static UpgradeData getUpgradeData(int modelID) {
      return (UpgradeData)dataMap.get(modelID);
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getTaregtID() {
      return this.taregtID;
   }

   public void setTaregtID(int taregtID) {
      this.taregtID = taregtID;
   }

   public HashMap getNeedItems() {
      return this.needItems;
   }

   public void setNeedItems(HashMap needItems) {
      this.needItems = needItems;
   }

   public int getMoney() {
      return this.money;
   }

   public void setMoney(int money) {
      this.money = money;
   }

   public List getShowItems() {
      return this.showItems;
   }

   public void setShowItems(List showItems) {
      this.showItems = showItems;
   }

   public int getRate() {
      return this.rate;
   }

   public void setRate(int rate) {
      this.rate = rate;
   }

   public int getIngot() {
      return this.ingot;
   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }
}
