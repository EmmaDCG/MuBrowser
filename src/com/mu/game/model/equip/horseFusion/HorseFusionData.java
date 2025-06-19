package com.mu.game.model.equip.horseFusion;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class HorseFusionData {
   public static int ProtectItemID = 1;
   private static Item protectShowItem = null;
   private static HashMap dataMap = new HashMap();
   private int level;
   private int starLevel;
   private int rate;
   private int money;

   public static void init(Sheet sheet) throws Exception {
      ProtectItemID = Tools.getCellIntValue(sheet.getCell("B1"));
      protectShowItem = ItemTools.createItem(ProtectItemID, 1, 2);
      if (protectShowItem == null) {
         throw new Exception(sheet.getName() + " - 坐骑融合保护道具不存在");
      }
   }

   public static int creatKey(int level, int starLevel) {
      return level * 100000 + starLevel;
   }

   public static HorseFusionData getFusionData(int level, int starLevel) {
      int key = creatKey(level, starLevel);
      return (HorseFusionData)dataMap.get(key);
   }

   public static Item getProtectShowItem() {
      return protectShowItem;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getStarLevel() {
      return this.starLevel;
   }

   public void setStarLevel(int starLevel) {
      this.starLevel = starLevel;
   }

   public int getRate() {
      return this.rate;
   }

   public void setRate(int rate) {
      this.rate = rate;
   }

   public int getMoney() {
      return this.money;
   }

   public void setMoney(int money) {
      this.money = money;
   }
}
