package com.mu.game.model.item.action.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.properties.levelData.PlayerLevelData;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;

public class AddExp extends ItemAction {
   private int value;
   private boolean percent;

   public AddExp(int value, boolean percent) {
      this.value = value;
      this.percent = percent;
   }

   public int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int result = 1;
      long exp = this.getActualValue(player);
      long addExp = PlayerManager.addExp(player, exp, -1L);
      if (addExp <= 0L) {
         result = 1018;
      }

      int count = 1;
      if (result == 1) {
         if (useNum > 1) {
            addExp = 0L;
            int countCanDo = Math.max(1, Math.min(item.getCount(), this.getRemainUseCount(player, item.getModelID())));

            for(int i = 2; i <= countCanDo; ++i) {
               exp = this.getActualValue(player);
               addExp = PlayerManager.addExp(player, exp, -1L);
               if (addExp <= 0L) {
                  break;
               }

               ++count;
            }
         }

         player.getItemManager().deleteItem(item, count, 20);
      }

      return result;
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      return PlayerManager.isMaxLevel(player) ? 1017 : 1;
   }

   private long getActualValue(Player player) {
      long exp = (long)this.value;
      if (this.percent) {
         exp = PlayerLevelData.getNeedExp(player.getLevel()) * (long)this.value / 100000L;
      }

      return exp;
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
      PlayerManager.addExp(player, this.getActualValue(player) * (long)count, -1L);
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }

   public boolean isPercent() {
      return this.percent;
   }

   public void setPercent(boolean percent) {
      this.percent = percent;
   }

   public void initCheck(String des) throws Exception {
      if (this.value < 1) {
         throw new Exception(des + " : 添加经验数值错误");
      }
   }
}
