package com.mu.game.model.equip.rune;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class RuneInheritData {
   private static HashMap inheritMap = new HashMap();
   private int number;
   private int itemID;
   private int count;
   private Item item = null;

   public RuneInheritData(int number, int itemID, int count) {
      this.number = number;
      this.itemID = itemID;
      this.count = count;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      int i;
      for(i = 2; i <= rows; ++i) {
         int number = Tools.getCellIntValue(sheet.getCell("A" + i));
         int itemID = Tools.getCellIntValue(sheet.getCell("B" + i));
         int count = Tools.getCellIntValue(sheet.getCell("C" + i));
         if (ItemModel.getModel(itemID) == null || count < 1) {
            throw new Exception(sheet.getName() + " ，道具不存在或者数量不合适，第 " + i + "行");
         }

         Item item = ItemTools.createItem(itemID, count, 2);
         RuneInheritData ri = new RuneInheritData(number, itemID, count);
         ri.setItem(item);
         inheritMap.put(number, ri);
      }

      for(i = 1; i <= RuneForgingData.getMaxcount(); ++i) {
         if (!inheritMap.containsKey(i)) {
            throw new Exception(sheet.getName() + ",数据不完整");
         }
      }

   }

   public static RuneInheritData getInherite(int number) {
      return (RuneInheritData)inheritMap.get(number);
   }

   public Item getItem() {
      return this.item;
   }

   public void setItem(Item item) {
      this.item = item;
   }

   public int getNumber() {
      return this.number;
   }

   public void setNumber(int number) {
      this.number = number;
   }

   public int getItemID() {
      return this.itemID;
   }

   public void setItemID(int itemID) {
      this.itemID = itemID;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public int getCount() {
      return this.count;
   }
}
