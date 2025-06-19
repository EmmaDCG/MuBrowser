package com.mu.game.model.item.action.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.pet.PetConfigManager;
import com.mu.game.model.unit.player.Player;
import java.util.ArrayList;
import java.util.List;

public class PetLevelUpOrAddItem extends ItemAction {
   private int targetLevel;
   private int itemModelID;
   private int count;

   public PetLevelUpOrAddItem(int targetLevel, int itemModelID, int count) {
      this.targetLevel = targetLevel;
      this.itemModelID = itemModelID;
      this.count = count;
   }

   protected int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int result = 1;
      if (player.getPetManager().getRank().getRank() >= this.targetLevel) {
         List unitList = new ArrayList();
         ItemDataUnit unit = new ItemDataUnit(this.itemModelID, this.count);
         unitList.add(unit);
         result = player.getItemManager().deleteItemAndAddModel(item, 1, 20, unitList).getResult();
         unitList.clear();
         unitList = null;
      } else {
         result = player.getPetManager().settingPetRank(this.targetLevel);
         if (result == 1) {
            player.getItemManager().deleteItem(item, 1, 20);
         }
      }

      return result;
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      return !player.getPetManager().isOpen() ? 22012 : 1;
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
   }

   public void initCheck(String des) throws Exception {
      if (!ItemModel.hasItemModel(this.itemModelID)) {
         throw new Exception(des + "-冰后精魂-想要添加的道具ID不存在");
      } else if (this.count < 1) {
         throw new Exception(des + "-冰后精魂-物品数量不合适");
      } else if (this.targetLevel < 1 || PetConfigManager.getRankSize() < this.targetLevel) {
         throw new Exception(des + "-冰后精魂-佣兵等级不正确");
      }
   }
}
