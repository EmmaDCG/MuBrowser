package com.mu.game.model.item.action.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;

public class RoleLevelUp extends ItemAction {
   private int targetLevel = 1;

   public RoleLevelUp(int targetLevel) {
      this.targetLevel = targetLevel;
   }

   protected int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int result = player.getItemManager().deleteItem(item, 1, 20).getResult();
      if (result == 1) {
         PlayerManager.levelUpDetail(player, this.targetLevel, 0L);
      }

      return result;
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      return player.getLevel() >= this.targetLevel ? 3055 : 1;
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
   }

   public void initCheck(String des) throws Exception {
      if (this.targetLevel < 1 || this.targetLevel > PlayerManager.getRoleMaxLevel()) {
         throw new Exception(des + "目标等级错误");
      }
   }
}
