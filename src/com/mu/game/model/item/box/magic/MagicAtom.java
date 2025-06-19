package com.mu.game.model.item.box.magic;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.box.BoxItemAtom;
import com.mu.game.model.item.box.BoxManager;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.weight.WeightElement;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class MagicAtom extends BoxItemAtom {
   private static HashMap atomMap = new HashMap();
   private int ID;
   private boolean record;
   private Item showItem = null;
   private boolean show = true;
   private WeightElement weightItems = null;

   public MagicAtom(int ID, boolean record, int itemID, int count, int weight, boolean isBind, int statRuleID, long expiredTime) {
      super(itemID, count, weight, isBind, statRuleID, expiredTime);
      this.ID = ID;
      this.record = record;
   }

   public static void initMagicAtom(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int ID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int itemID = Tools.getCellIntValue(sheet.getCell("B" + i));
         int count = Tools.getCellIntValue(sheet.getCell("C" + i));
         int weight = Tools.getCellIntValue(sheet.getCell("D" + i));
         boolean bind = Tools.getCellIntValue(sheet.getCell("E" + i)) == 1;
         int statRuleID = Tools.getCellIntValue(sheet.getCell("F" + i));
         long expiredTime = Tools.getCellLongValue(sheet.getCell("G" + i));
         boolean record = Tools.getCellFloatValue(sheet.getCell("H" + i)) == 1.0F;
         String realItems = Tools.getCellValue(sheet.getCell("I" + i));
         boolean show = Tools.getCellIntValue(sheet.getCell("J" + i)) == 1;
         String des = sheet.getName() + "-第 " + i + " 行：";
         if (!ItemModel.hasItemModel(itemID)) {
            throw new Exception(des + "显示物品ID不存在");
         }

         if (count < 1) {
            throw new Exception(des + "显示物品数量不正确");
         }

         if (weight < 1) {
            throw new Exception(des + "显示物品权重不正确");
         }

         if (expiredTime < 0L && expiredTime != -1L) {
            throw new Exception(des + "显示物品的过期时间不正确");
         }

         WeightElement weightItems = BoxManager.createWeightElement(realItems, des);
         if (weightItems == null) {
            throw new Exception(des + "显示道具填写错误");
         }

         MagicAtom atom = new MagicAtom(ID, record, itemID, count, weight, bind, statRuleID, expiredTime);
         ItemDataUnit unit = atom.getDataUnit();
         unit.setHide(true);
         Item showItem = ItemTools.createItem(2, unit);
         if (showItem == null) {
            throw new Exception(des + "显示物品不存在");
         }

         ItemTools.setSystemExpire(showItem, unit.getExpireTime());
         atom.setWeightItems(weightItems);
         atom.setShowItem(showItem);
         atom.setShow(show);
         atomMap.put(atom.getID(), atom);
      }

   }

   public ItemDataUnit getRndItemData() {
      BoxItemAtom atom = (BoxItemAtom)this.weightItems.getRndAtom();
      ItemDataUnit unit = atom.getDataUnit();
      unit.setSource(13);
      return unit;
   }

   public Item getShowItem() {
      return this.showItem;
   }

   public void setShowItem(Item showItem) {
      this.showItem = showItem;
   }

   public static MagicAtom getMagicAtom(int atomID) {
      return (MagicAtom)atomMap.get(atomID);
   }

   public int getID() {
      return this.ID;
   }

   public void setID(int iD) {
      this.ID = iD;
   }

   public boolean isRecord() {
      return this.record;
   }

   public void setRecord(boolean record) {
      this.record = record;
   }

   public WeightElement getWeightItems() {
      return this.weightItems;
   }

   public void setWeightItems(WeightElement weightItems) {
      this.weightItems = weightItems;
   }

   public boolean isShow() {
      return this.show;
   }

   public void setShow(boolean show) {
      this.show = show;
   }
}
