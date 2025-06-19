package com.mu.game.model.item.action.imp;

import com.mu.config.Constant;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.packet.CreatureHpMpPacket;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;

public class AddMp extends ItemAction {
   private int value;
   private boolean percent;

   public AddMp(int value, boolean percent) {
      this.value = value;
      this.percent = percent;
   }

   public int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int result = player.getItemManager().deleteItem(item, useNum, 20).getResult();
      if (result != 1) {
         return result;
      } else {
         int beingValue = this.getBeingAddMp(player);
         if (beingValue > 0) {
            addMPAndSend(player, beingValue);
         }

         return result;
      }
   }

   public static void addMPAndSend(Creature creature, int addValue) {
      creature.increaseMp(addValue);
      CreatureHpMpPacket.creatureMpChange(creature);
   }

   public static void reduceMPAndSend(Creature creature, int value) {
      creature.decreaseMp(value);
      CreatureHpMpPacket.creatureMpChange(creature);
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      if (player.getMp() >= player.getMaxMp()) {
         return 1010;
      } else {
         return player.isDie() ? 8028 : 1;
      }
   }

   private int getBeingAddMp(Player player) {
      int result = player.getMaxMp() - player.getMp();
      int actualDrug = this.value;
      if (this.percent) {
         actualDrug = Constant.getPercentValue(player.getMaxMp(), this.value);
      }

      result = Math.min(result, actualDrug);
      return result;
   }

   public int getValue() {
      return this.value;
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
   }

   public void initCheck(String des) throws Exception {
      if (this.value < 1) {
         throw new Exception(des + " : 添加蓝数值错误");
      }
   }
}
