package com.mu.io.game.packet.imp.equip;

import com.mu.db.log.IngotChangeType;
import com.mu.game.model.equip.star.StarInheritData;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class InheritEquipment extends ReadAndWritePacket {
   public InheritEquipment(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long targetItemID = (long)this.readDouble();
      long divertItemID = (long)this.readDouble();
      Item targetItem = StrengthEquipment.getForgingItem(player, targetItemID);
      Item divertItem = StrengthEquipment.getForgingItem(player, divertItemID);
      int result = this.canInherit(player, targetItem, divertItem);
      if (result == 1) {
         player.getItemManager().updateStarLevel(targetItem, divertItem.getStarLevel(), divertItem.getRunes());
         player.getItemManager().deleteItem(divertItem, 21);
         StarInheritData data = StarInheritData.getData(divertItem.getStarLevel());
         switch(data.getMoneyType()) {
         case 2:
            PlayerManager.reduceIngot(player, data.getMoney(), IngotChangeType.InheriteEquipment, "");
            break;
         default:
            PlayerManager.reduceMoney(player, data.getMoney());
         }
      } else {
         SystemMessage.writeMessage(player, result);
      }

      this.writeDouble((double)targetItemID);
      this.writeBoolean(result == 1);
      player.writePacket(this);
   }

   private int canInherit(Player player, Item targetItem, Item divertItem) {
      if (targetItem != null && targetItem.getCount() >= 1) {
         if (divertItem != null && divertItem.getCount() >= 1) {
            if (targetItem.getID() == divertItem.getID()) {
               return 5031;
            } else {
               divertItem.getContainerType();
               if (!StarInheritData.canInherit(targetItem.getItemType())) {
                  return 5007;
               } else if (!StarInheritData.canInherit(divertItem.getItemType())) {
                  return 5008;
               } else if (divertItem.getStarLevel() < StarInheritData.getMinStar()) {
                  return 5012;
               } else if (targetItem.getStarLevel() >= divertItem.getStarLevel()) {
                  return 5011;
               } else if (divertItem.getStones().size() > 0) {
                  return 5009;
               } else {
                  StarInheritData data = StarInheritData.getData(divertItem.getStarLevel());
                  switch(data.getMoneyType()) {
                  case 2:
                     if (player.getIngot() < data.getMoney()) {
                        return 1015;
                     }
                     break;
                  default:
                     if (!PlayerManager.hasEnoughMoney(player, data.getMoney())) {
                        return 1011;
                     }
                  }

                  return 1;
               }
            }
         } else {
            return 3002;
         }
      } else {
         return 3002;
      }
   }
}
