package com.mu.io.game.packet.imp.equip;

import com.mu.executor.imp.log.ItemForgingExecutor;
import com.mu.game.model.equip.horseFusion.HorseFusionData;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.List;

public class HorseFusion extends ReadAndWritePacket {
   public HorseFusion(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long targetID = (long)this.readDouble();
      long materialID = (long)this.readDouble();
      boolean useProtect = this.readBoolean();
      Item targetItem = StrengthEquipment.getForgingItem(player, targetID);
      Item materialItem = StrengthEquipment.getForgingItem(player, materialID);
      int result = this.canFuse(player, targetItem, materialItem, useProtect);
      if (result == 1) {
         player.getItemManager().updateStarLevel(targetItem, materialItem.getStarLevel(), (List)null);
         if (useProtect) {
            player.getItemManager().updateStarLevel(materialItem, 0, (List)null);
            player.getItemManager().deleteItemByModel(HorseFusionData.ProtectItemID, 1, 21);
         } else {
            player.getItemManager().deleteItem(materialItem, 21);
         }

         ItemForgingExecutor.logFoging(player, targetItem, targetItem.getStarLevel(), 5, true, 0, 0);
      }

      this.writeBoolean(result == 1);
      this.writeDouble((double)targetID);
      this.writeBoolean(!useProtect);
      player.writePacket(this);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   private int canFuse(Player player, Item targetItem, Item materialItem, boolean useProtect) {
      if (targetItem != null && targetItem.getCount() >= 1) {
         if (materialItem != null && materialItem.getCount() >= 1) {
            if (targetItem.getID() == materialItem.getID()) {
               return 5059;
            } else if (!targetItem.isTimeLimited() && !materialItem.isTimeLimited()) {
               if (targetItem.getItemType() != 24) {
                  return 5064;
               } else if (materialItem.getItemType() != 24) {
                  return 5065;
               } else if (materialItem.getStarLevel() <= targetItem.getStarLevel()) {
                  return 5060;
               } else {
                  return useProtect && !player.getBackpack().hasEnoughItem(HorseFusionData.ProtectItemID, 1) ? 5063 : 1;
               }
            } else {
               return 5068;
            }
         } else {
            return 5062;
         }
      } else {
         return 5061;
      }
   }
}
