package com.mu.game.model.item.box.magic.market;

import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.shop.Goods;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;

public class MagicMarketGoods extends Goods {
   private static HashMap magicGoods = new HashMap();
   private static List goodsList = new ArrayList();
   private int magicID;

   public MagicMarketGoods(int magicID) {
      this.magicID = magicID;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int magicID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int modelID = Tools.getCellIntValue(sheet.getCell("B" + i));
         int money = Tools.getCellIntValue(sheet.getCell("C" + i));
         int statID = Tools.getCellIntValue(sheet.getCell("D" + i));
         long expireTime = Tools.getCellLongValue(sheet.getCell("E" + i));
         boolean bind = Tools.getCellIntValue(sheet.getCell("F" + i)) == 1;
         if (!ItemModel.hasItemModel(modelID)) {
            throw new Exception("积分兑换 - " + sheet.getName() + "-物品道具不存在 ，第" + i + "行");
         }

         if (money < 1) {
            throw new Exception("积分兑换 - " + sheet.getName() + "-所需积分数量不合理 ，第" + i + "行");
         }

         if (expireTime != -1L && expireTime <= 0L) {
            throw new Exception("积分兑换 - " + sheet.getName() + "-限时填写不正确 ，第" + i + "行");
         }

         MagicMarketGoods goods = new MagicMarketGoods(magicID);
         goods.setExpireTime(expireTime);
         goods.setModelID(modelID);
         goods.setMoneyType(5);
         goods.setPrice(money);
         goods.setStatRuleID(statID);
         goods.setBind(bind);
         boolean flag = goods.entity();
         if (!flag) {
            throw new Exception("积分兑换 - " + sheet.getName() + "-道具实体无法生成 ，第" + i + "行");
         }

         magicGoods.put(goods.getGoodsID(), goods);
         goodsList.add(goods);
      }

   }

   public ItemDataUnit getUnit() {
      ItemDataUnit unit = super.getUnit();
      unit.setSource(36);
      unit.setBind(this.isBind());
      return unit;
   }

   public static MagicMarketGoods getGoods(long itemID) {
      return (MagicMarketGoods)magicGoods.get(itemID);
   }

   public static HashMap getMagicGoods() {
      return magicGoods;
   }

   public static List getGoodsList() {
      return goodsList;
   }

   public int getMagicID() {
      return this.magicID;
   }

   public void setMagicID(int magicID) {
      this.magicID = magicID;
   }
}
