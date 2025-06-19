package com.mu.game.model.item.action.imp;

import com.mu.db.log.IngotChangeType;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;

public class AddMoney extends ItemAction {
   private int moneyType;
   private int value;

   public AddMoney(int moneyType, int value) {
      this.moneyType = moneyType;
      this.value = value;
   }

   public int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int count = this.getActualCount(player, item.getModelID(), useNum);
      if (count < 1) {
         return 8;
      } else {
         int result = player.getItemManager().deleteItem(item, count, 20).getResult();
         if (result == 1) {
            int money = this.getActualValue(player, count);
            this.addMoney(player, money);
         }

         return result;
      }
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      return 1;
   }

   private int getActualCount(Player player, int modelID, int wantCount) {
      if (wantCount <= 1) {
         return wantCount;
      } else {
         int count = Math.min(wantCount, this.getRemainUseCount(player, modelID));
         long maxMoney = (long)Math.min(Integer.MAX_VALUE - player.getMoney(), count * this.getValue());
         switch(this.getMoneyType()) {
         case 2:
            maxMoney = (long)Math.min(Integer.MAX_VALUE - player.getIngot(), count * this.getValue());
         case 3:
         default:
            break;
         case 4:
            maxMoney = (long)Math.min(Integer.MAX_VALUE - player.getBindIngot(), count * this.getValue());
         }

         count = Math.max(1, (int)(maxMoney / (long)this.getValue()));
         return count;
      }
   }

   private int getActualValue(Player player, int count) {
      long money = (long)this.getValue() * 1L * (long)count;
      switch(this.getMoneyType()) {
      case 1:
         money = Math.min(money, (long)(Integer.MAX_VALUE - player.getMoney()));
         break;
      case 2:
         money = Math.min(money, (long)(Integer.MAX_VALUE - player.getIngot()));
         break;
      case 3:
      default:
         money = 0L;
         break;
      case 4:
         money = Math.min(money, (long)(Integer.MAX_VALUE - player.getBindIngot()));
      }

      return (int)money;
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
      int money = this.getActualValue(player, count);
      this.addMoney(player, money);
   }

   public void addMoney(Player player, int money) {
      switch(this.getMoneyType()) {
      case 1:
         PlayerManager.addMoney(player, money);
         break;
      case 2:
         PlayerManager.addIngot(player, money, IngotChangeType.USEITEM.getType());
      case 3:
      default:
         break;
      case 4:
         PlayerManager.addBindIngot(player, money, IngotChangeType.USEITEM.getType());
      }

   }

   public int getMoneyType() {
      return this.moneyType;
   }

   public int getValue() {
      return this.value;
   }

   public void initCheck(String des) throws Exception {
      if (this.value < 1) {
         throw new Exception(des + " : 添加金钱，数值错误");
      }
   }
}
