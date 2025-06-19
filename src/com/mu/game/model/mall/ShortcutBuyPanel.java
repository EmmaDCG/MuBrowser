package com.mu.game.model.mall;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.shop.Goods;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;

public class ShortcutBuyPanel {
   private static HashMap panelMap = new HashMap();
   private static HashMap panelItemMap = new HashMap();
   private int panelID;
   private String title;
   private List itemList = null;

   public ShortcutBuyPanel(int panelID, String title, List itemList) {
      this.panelID = panelID;
      this.title = title;
      this.itemList = itemList;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int panelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String title = Tools.getCellValue(sheet.getCell("B" + i));
         String itemStr = Tools.getCellValue(sheet.getCell("C" + i));
         String des = sheet.getName() + " 第 " + i + " 行";
         List itemList = null;
         String[] splits = itemStr.split(";");
         String[] var12 = splits;
         int var11 = splits.length;

         for(int var10 = 0; var10 < var11; ++var10) {
            String s = var12[var10];
            String[] secSplits = s.split(",");
            int itemID = Integer.parseInt(secSplits[0]);
            int money = Integer.parseInt(secSplits[1]);
            boolean bind = Integer.parseInt(secSplits[2]) == 1;
            int moneyType = Integer.parseInt(secSplits[3]);
            long expireTime = Long.parseLong(secSplits[4]);
            Item item = addSellItem(des, itemID, money, moneyType, bind, expireTime);
            if (itemList == null) {
               itemList = new ArrayList();
            }

            itemList.add(item);
         }

         if (itemList == null) {
            throw new Exception(des + "道具没有填写");
         }

         ShortcutBuyPanel sbp = new ShortcutBuyPanel(panelID, title, itemList);
         panelMap.put(panelID, sbp);
      }

   }

   public static Item addSellItem(String des, int modelID, int money, int moneyType, boolean isBind, long expireTime) throws Exception {
      if (!ItemModel.hasItemModel(modelID)) {
         throw new Exception(des + " 道具模板不存在");
      } else if (money < 1) {
         throw new Exception(des + "金钱不合理");
      } else {
         switch(moneyType) {
         case 1:
         case 2:
         case 4:
         case 5:
            Goods goods = new Goods();
            goods.setLabel(1);
            goods.setExpireTime(expireTime);
            goods.setModelID(modelID);
            goods.setMoneyType(moneyType);
            goods.setPrice(money);
            goods.setBind(isBind);
            boolean flag = goods.entity();
            if (flag) {
               panelItemMap.put(goods.getGoodsID(), goods);
               return goods.getGoodItem();
            }

            throw new Exception(des + "-物品初始化出错");
         case 3:
         default:
            throw new Exception(des + "金钱类型不正确");
         }
      }
   }

   public static ShortcutBuyPanel getShortcutBuyPanel(int panelID) {
      return (ShortcutBuyPanel)panelMap.get(panelID);
   }

   public static boolean hasShortcutBuy(int panelID) {
      return panelMap.containsKey(panelID);
   }

   public static Goods getSellItem(long itemID) {
      return (Goods)panelItemMap.get(itemID);
   }

   public int getPanelID() {
      return this.panelID;
   }

   public void setPanelID(int panelID) {
      this.panelID = panelID;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public List getItemList() {
      return this.itemList;
   }

   public void setItemList(List itemList) {
      this.itemList = itemList;
   }

   public static HashMap getPanelMap() {
      return panelMap;
   }
}
