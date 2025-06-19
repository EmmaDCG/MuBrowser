package com.mu.game.model.item.box;

import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.weight.WeightAtom;

public class BoxItemAtom extends WeightAtom {
   private int itemID;
   private int count;
   private boolean isBind;
   private int statRuleID;
   private long expiredTime;

   public BoxItemAtom(int itemID, int count, int weight, boolean isBind, int statRuleID, long expiredTime) {
      super(weight);
      this.isBind = isBind;
      this.itemID = itemID;
      this.count = count;
      this.statRuleID = statRuleID;
      this.expiredTime = expiredTime;
   }

   public static BoxItemAtom createAtom(String keyDes, String str) throws Exception {
      String[] splits = str.split(",");
      if (splits.length < 6) {
         throw new Exception(keyDes + " : 数据不完整，长度不对" + str);
      } else {
         int itemID = Integer.parseInt(splits[0]);
         int count = Integer.parseInt(splits[1]);
         int weight = Integer.parseInt(splits[2]);
         boolean isBind = Integer.parseInt(splits[3]) == 1;
         int sourceID = Integer.parseInt(splits[4]);
         long expiredTime = Long.parseLong(splits[5]);
         if (!ItemModel.hasItemModel(itemID)) {
            throw new Exception(keyDes + " : 物品不存在");
         } else if (count < 1) {
            throw new Exception(keyDes + " : 物品数量不正确");
         } else if (weight < 1) {
            throw new Exception(keyDes + " : 权重或者概率不正确");
         } else if (expiredTime != -1L && expiredTime < 0L) {
            throw new Exception(keyDes + " : 过期时间填写不正确");
         } else {
            BoxItemAtom atom = new BoxItemAtom(itemID, count, weight, isBind, sourceID, expiredTime);
            return atom;
         }
      }
   }

   public ItemDataUnit getDataUnit() {
      ItemDataUnit unit = new ItemDataUnit(this.itemID, this.count);
      unit.setSource(28);
      unit.setStatRuleID(this.statRuleID);
      unit.setBind(this.isBind);
      unit.setExpireTime(this.getExpiredTime());
      return unit;
   }

   public int getItemID() {
      return this.itemID;
   }

   public void setItemID(int itemID) {
      this.itemID = itemID;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public boolean isBind() {
      return this.isBind;
   }

   public void setBind(boolean isBind) {
      this.isBind = isBind;
   }

   public int getStatRuleID() {
      return this.statRuleID;
   }

   public void setStatRuleID(int statRuleID) {
      this.statRuleID = statRuleID;
   }

   public long getExpiredTime() {
      return this.expiredTime;
   }

   public void setExpiredTime(long expiredTime) {
      this.expiredTime = expiredTime;
   }
}
