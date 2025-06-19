package com.mu.game.model.item.action.imp;

import com.mu.config.Constant;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.attack.CreatureHpChange;

public class AddHp extends ItemAction {
   private int value;
   private boolean percent;

   public AddHp(int value, boolean percent) {
      this.value = value;
      this.percent = percent;
   }

   public int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int result = player.getItemManager().deleteItem(item, useNum, 20).getResult();
      if (result != 1) {
         return result;
      } else {
         int beingValue = this.getBeingAddHp(player);
         if (beingValue > 0) {
            addHPAndSend(player, beingValue);
         }

         return result;
      }
   }

   public static void addHPAndSend(Creature creature, int addValue) {
      creature.increaseHp(addValue);
      AttackResult ar = new AttackResult(6, addValue, 0, -1, creature);
      creature.getUnitType();
      CreatureHpChange.sendToClient(creature, ar);
   }

   public static void reduceHp(Creature creature, int hpValue, Creature attacker) {
      AttackResult result = new AttackResult(1, hpValue, 0, -1, attacker);
      reduceHp(creature, result, attacker);
   }

   public static void reduceHp(Creature creature, AttackResult result, Creature attacker) {
      if (!creature.isDie() && !creature.isDestroy() && !creature.isDamageImmunity()) {
         creature.decreaseHp(attacker, result);
         creature.getUnitType();
         CreatureHpChange.sendToClient(creature, result);
      }
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      if (player.getHp() >= player.getMaxHp()) {
         return 1009;
      } else {
         return player.isDie() ? 8028 : 1;
      }
   }

   private int getBeingAddHp(Player player) {
      int result = player.getMaxHp() - player.getHp();
      int actualDrug = this.value;
      if (this.percent) {
         actualDrug = Constant.getPercentValue(player.getMaxHp(), this.value);
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
         throw new Exception(des + " : 添加 血 数值不正确");
      }
   }
}
