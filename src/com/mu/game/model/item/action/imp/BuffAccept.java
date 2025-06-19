package com.mu.game.model.item.action.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.buff.Buff;
import com.mu.game.model.unit.buff.model.BuffDynamicData;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.player.Player;
import java.util.List;

public class BuffAccept extends ItemAction {
   private int buffID;
   private int level;

   public BuffAccept(int buffID, int level) {
      this.buffID = buffID;
      this.level = level;
   }

   protected int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int result = player.getItemManager().deleteItem(item, 1, 20).getResult();
      if (result != 1) {
         return result;
      } else {
         this.acceptBuff(player);
         return 1;
      }
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      BuffModel model = BuffModel.getModel(this.buffID);
      if (model.getBuffType() == 3) {
         Buff hasBuff = player.getBuffManager().getBuff(this.buffID);
         if (!model.isOverlap()) {
            if (hasBuff != null) {
               return 3038;
            }
         } else if (!hasBuff.canOverlap()) {
            return 3039;
         }
      }

      return 1;
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
      Buff buff = player.getBuffManager().getBuff(this.buffID);
      if (buff == null) {
         this.acceptBuff(player);
      }
   }

   private void acceptBuff(Player player) {
      boolean send = true;
      if (!player.isEnterMap()) {
         send = false;
      }

      player.getBuffManager().createAndStartBuff(player, this.buffID, this.level, send, 0L, (List)null);
   }

   public void endAction(Player player) {
      player.getBuffManager().endBuff(this.buffID, true);
   }

   public void initCheck(String des) throws Exception {
      if (!BuffModel.hasModel(this.buffID)) {
         throw new Exception(des + " : buffID 不存在");
      } else {
         BuffDynamicData dyData = BuffDynamicData.getDyData(this.buffID, this.level);
         if (dyData == null) {
            throw new Exception(des + " : buff等级数据没有填写");
         }
      }
   }

   public int getBuffID() {
      return this.buffID;
   }

   public void setBuffID(int buffID) {
      this.buffID = buffID;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }
}
