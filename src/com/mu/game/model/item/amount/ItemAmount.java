package com.mu.game.model.item.amount;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.vip.effect.VIPEffectType;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class ItemAmount {
   private static HashMap amounts = new HashMap();
   private int modelID;
   private int limitCount;
   private int vipEffectType = -1;

   public ItemAmount(int modelID, int limitCount, int vipEffectType) {
      this.modelID = modelID;
      this.limitCount = limitCount;
      this.vipEffectType = vipEffectType;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int modelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int limitCount = Tools.getCellIntValue(sheet.getCell("B" + i));
         int vipEffectType = Tools.getCellIntValue(sheet.getCell("C" + i));
         if (limitCount < 1) {
            throw new Exception("道具使用数量限制 - " + modelID);
         }

         ItemAmount amount = new ItemAmount(modelID, limitCount, vipEffectType);
         amounts.put(modelID, amount);
      }

   }

   public static ItemAmount getAmount(int modelID) {
      return (ItemAmount)amounts.get(modelID);
   }

   public int getLimitCountByPlayer(Player player) {
      int count = this.getLimitCount();
      VIPEffectType vipType = VIPEffectType.valueOf(this.vipEffectType);
      if (vipType != null) {
         count += player.getVIPManager().getEffectIntegerValue(vipType);
      }

      return count;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   private int getLimitCount() {
      return this.limitCount;
   }

   public void setLimitCount(int limitCount) {
      this.limitCount = limitCount;
   }

   public int getVipEffectType() {
      return this.vipEffectType;
   }

   public void setVipEffectType(int vipEffectType) {
      this.vipEffectType = vipEffectType;
   }
}
