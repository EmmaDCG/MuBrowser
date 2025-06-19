package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.horseFusion.HorseFusionData;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class RequestHorseFuse extends ReadAndWritePacket {
   public RequestHorseFuse(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long targetID = (long)this.readDouble();
      long materialID = (long)this.readDouble();
      Item targetItem = StrengthEquipment.getForgingItem(player, targetID);
      Item materialItem = StrengthEquipment.getForgingItem(player, materialID);
      int result = this.canFuse(player, targetItem, materialItem);
      this.writeBoolean(result == 1);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      } else {
         Item showItem = targetItem.cloneItem(2);
         showItem.changeStarLevel(materialItem.getStarLevel());
         GetItemStats.writeItem(showItem, this);
         GetItemStats.writeItem(HorseFusionData.getProtectShowItem(), this);
         showItem.destroy();
         showItem = null;
      }

      player.writePacket(this);
   }

   private int canFuse(Player player, Item targetItem, Item materialItem) {
      if (targetItem != null && targetItem.getCount() >= 1) {
         if (materialItem != null && materialItem.getCount() >= 1) {
            if (targetItem.getID() == materialItem.getID()) {
               return 5059;
            } else if (targetItem.getItemType() != 24) {
               return 5064;
            } else if (materialItem.getItemType() != 24) {
               return 5065;
            } else {
               return materialItem.getStarLevel() <= targetItem.getStarLevel() ? 5060 : 1;
            }
         } else {
            return 5062;
         }
      } else {
         return 5061;
      }
   }

   public static int canFuse(Item item) {
      if (item != null && item.getCount() >= 1) {
         if (item.isTimeLimited()) {
            return 5068;
         } else {
            return item.getItemType() == 24 ? 1 : 5061;
         }
      } else {
         return 3002;
      }
   }
}
