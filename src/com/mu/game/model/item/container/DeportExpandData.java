package com.mu.game.model.item.container;

import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class DeportExpandData {
   private static HashMap deportDatas = new HashMap();
   private int page;
   private int itemID;
   private int count;

   public DeportExpandData(int page, int itemID, int count) {
      this.page = page;
      this.itemID = itemID;
      this.count = count;
   }

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet bpSheet = wb.getSheet(1);
      Sheet deportSheet = wb.getSheet(2);
      BackpackExpandData.init(bpSheet);
      initSheet(deportSheet);
   }

   private static void initSheet(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int page = Tools.getCellIntValue(sheet.getCell("A" + i));
         i = Tools.getCellIntValue(sheet.getCell("B" + i));
         int count = Tools.getCellIntValue(sheet.getCell("C" + i));
         if (ItemModel.getModel(i) == null || count < 1) {
            throw new Exception(sheet.getName() + " - 所需物品不存在 或者物品不合适,第" + i + "行");
         }

         DeportExpandData data = new DeportExpandData(page, i, count);
         deportDatas.put(page, data);
      }

      int page = 3;
      HashMap map = deportDatas;

      for(int i = 1; i <= page; ++i) {
         if (!map.containsKey(Integer.valueOf(page))) {
            throw new Exception(sheet.getName() + "，开启第" + i + "页数据不够");
         }
      }

   }

   public static int canOpen(Player player) {
      DeportExpandData data = null;
      int page = player.getDepot().getPage();
      if (page >= 4) {
         return 2001;
      } else {
         data = getDeportExpandData(page);
         return data == null ? 2002 : 1;
      }
   }

   public static int addPage(Player player) {
      int result = canOpen(player);
      if (result != 1) {
         return result;
      } else {
         int page = player.getDepot().getPage();
         DeportExpandData data = getDeportExpandData(page);
         result = player.getItemManager().deleteItemByModel(data.getItemID(), data.getCount(), 20).getResult();
         return result;
      }
   }

   public static DeportExpandData getDeportExpandData(int page) {
      return (DeportExpandData)deportDatas.get(page);
   }

   public void setPage(int page) {
      this.page = page;
   }

   public int getPage() {
      return this.page;
   }

   public void setItemID(int itemID) {
      this.itemID = itemID;
   }

   public int getItemID() {
      return this.itemID;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public int getCount() {
      return this.count;
   }
}
